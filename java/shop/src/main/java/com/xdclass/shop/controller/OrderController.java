package com.xdclass.shop.controller;

import com.xdclass.couponserviceapi.dto.UserCouponInfoDto;
import com.xdclass.couponserviceapi.service.ICouponService;
import com.xdclass.shop.common.Constants;
import com.xdclass.shop.common.Page;
import com.xdclass.shop.common.web.JsonResult;
import com.xdclass.shop.dto.UserCouponDTO;
import com.xdclass.shop.model.Order;
import com.xdclass.shop.model.OrderItem;
import com.xdclass.shop.model.User;
import com.xdclass.shop.model.UserAddress;
import com.xdclass.shop.service.CouponService;
import com.xdclass.shop.service.OrderService;
import com.xdclass.shop.service.UserAddressService;
import com.xdclass.shop.service.UserService;
import com.xdclass.shop.util.CartUtil;
import com.xdclass.shop.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Daniel
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserAddressService userAddressService;

    @Autowired
    private CouponService couponService;


    @Reference
    private ICouponService iCouponService;

    /**
     * 订单列表
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Model model, HttpSession session, HttpServletRequest request) {
        User user = UserUtil.getUserFromSession(session);
        Page<Order> page = new Page<Order>(request);
        orderService.findOrders(page, user.getId());
        model.addAttribute("page", page);
        return "order/orderList";
    }

    /**
     * 订单确认
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/purchase", method = RequestMethod.GET)
    public String purchase(Model model, HttpSession session) {
        User user = userService.findOne(UserUtil.getUserFromSession(session).getId());
        List<UserAddress> userAddressList = user.getAddresses();
        List<UserCouponInfoDto> userCouponDTOS = iCouponService.userCouponList(user.getId());
        model.addAttribute("addressList", userAddressList);
        model.addAttribute("userCouponList", userCouponDTOS);
        return "order/orderPurchase";
    }

    /**
     * 下单
     *
     * @param address
     * @param session
     * @return
     */
    @RequestMapping(value = "/ordering", method = RequestMethod.POST)
    public String ordering(UserAddress address, HttpSession session, String userCouponCode) {
        System.out.println("收到优惠券" + userCouponCode);
        Order order = new Order();
        order.setCreateTime(new Date());
        address.setUser(UserUtil.getUserFromSession(session));
        order.setOrderNumber(new DateTime().toString("yyyyMMddHHmmSS"));
        order.setStatus(Constants.OrderStatus.WAIT_PAY);
        List<OrderItem> oiList = CartUtil.getOrderItemFromCart(session);
        BigDecimal totalSum = new BigDecimal("0");
        for (OrderItem oi : oiList) {
            totalSum = totalSum.add(new BigDecimal(oi.getProduct().getPoint() * oi.getQuantity()));
            oi.setOrder(order);
        }
        order.setTotalPrice(totalSum.doubleValue());
        order.setFinalPrice(totalSum.doubleValue());
        order.setOrderItems(oiList);
        order.setUser(UserUtil.getUserFromSession(session));
        //地址保存
        order.setAddress(address.getAddress());
        order.setZipcode(address.getZipcode());
        order.setConsignee(address.getConsignee());
        order.setPhone(address.getPhone());
        String ret = orderService.addOrder(order, oiList, address, userCouponCode);
        if (StringUtils.isNotBlank(ret)) {
            return ret;
        } else {
            CartUtil.cleanCart(session);
            return "order/orderingSuccess";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewOrder(@PathVariable Integer id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return "order/orderDetail";
    }

    @RequestMapping(value = "/pay/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult pay(@PathVariable(value = "id") Integer orderId, HttpSession session) {
        JsonResult result = new JsonResult();
        //验证订单是否归当前人员所有
        if (orderService.checkOwned(orderId, UserUtil.getUserFromSession(session).getId())) {
            orderService.pay(orderId);
            result.setToSuccess();
        } else {
            result.setToFail();
        }
        return result;
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult cancel(@PathVariable(value = "id") Integer orderId) {
        //TODO 验证是否拥有此订单
        orderService.updateOrderStatus(orderId, Constants.OrderStatus.DELETED);

        JsonResult result = new JsonResult();
        result.setToSuccess();
        return result;
    }

    /**
     * 订单收货确认
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/confirm/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult confirm(@PathVariable(value = "id") Integer orderId) {
        orderService.updateOrderStatus(orderId, Constants.OrderStatus.ENDED);

        JsonResult result = new JsonResult();
        result.setToSuccess();
        return result;
    }

}
