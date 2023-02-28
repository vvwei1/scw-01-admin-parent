package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TestHandler {
    @Autowired
    private AdminService adminService;
//    获取日志对象
    Logger logger = LoggerFactory.getLogger(TestHandler.class);


    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap){
        List<Admin> adminList=adminService.getAll();
        modelMap.addAttribute("adminList", adminList);
        return "sussess";
    }
    @RequestMapping("/send/array.html")
    public String testReceiveArrayOne(@RequestParam("array") List<Integer> array){
        for (Integer integer : array) {
            System.out.println(integer);
        }
        return "sussess";
    }
}
