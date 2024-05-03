package com.mall.sso.service.impl;

import com.github.pagehelper.PageHelper;
import com.mall.api.CommonPage;
import com.mall.sso.dto.UmsMenuNode;
import com.mall.sso.mapper.UmsMenuMapper;
import com.mall.sso.model.UmsMenu;
import com.mall.sso.model.UmsMenuExample;
import com.mall.sso.service.UmsMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
public class UmsMenuServiceImpl implements UmsMenuService {
    @Autowired
    private UmsMenuMapper menuMapper;

    @Override
    public boolean create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        int count = menuMapper.insert(umsMenu);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            umsMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            UmsMenu parentMenu = menuMapper.selectByPrimaryKey(umsMenu.getParentId());
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public boolean update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        updateLevel(umsMenu);
        int count = menuMapper.updateByPrimaryKeySelective(umsMenu);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }
    // TODO  返回值必须是page
    @Override
    public CommonPage list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsMenuExample example = new UmsMenuExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        List<UmsMenu> umsMenus = menuMapper.selectByExample(example);
        return CommonPage.restPage(umsMenus);
    }
    @Override
    public List<UmsMenuNode> treeList() {
        List<UmsMenu> menuList = menuMapper.selectByExample(new UmsMenuExample());
        List<UmsMenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList))
                .collect(Collectors.toList());
        return result;
    }
    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        int count = menuMapper.updateByPrimaryKeySelective(umsMenu);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }

    @Override
    public UmsMenu getById(Long id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean removeById(Long id) {
        int count = menuMapper.deleteByPrimaryKey(id);
        if(count == 0){
            return false;
        }else
        {
            return true;
        }
    }

    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     */
    private UmsMenuNode covertMenuNode(UmsMenu menu, List<UmsMenu> menuList) {
        UmsMenuNode node = new UmsMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<UmsMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
