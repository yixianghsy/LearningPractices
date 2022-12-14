package com.tuling.crossorigin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CrossOriginController {


    /**
     * 简单请求1
     *
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/ajaxGetReq1", method = RequestMethod.GET)
    @ResponseBody
    public String ajaxGetReq(HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin","http://localhost:8080");
        return "plain text from ajax";
    }

    /**
     * 简单请求2
     *
     * @param user
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/ajaxPostReq2", method = RequestMethod.POST)
    @ResponseBody
    public User ajaxPostReq2(User user, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin","http://localhost:8080");
        return user;
    }

    /**
     * 复杂请求
     *
     * @param user
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/ajaxPostReq", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxPostReq(User user, HttpServletResponse response) throws IOException {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        return "plain text from ajax";
    }

    @RequestMapping("/jsonpReq")
    @ResponseBody
    public String doBiz(HttpServletResponse response) throws IOException {
        return "myFunc({ \"id\":\"0001\", \"name\":\"Alex\" })";
    }

}

class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}