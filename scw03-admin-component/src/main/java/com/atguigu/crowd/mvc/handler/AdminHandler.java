package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {
    @Autowired
    AdminService adminService;

    /*
    * 管理员登陆
    * */
    @RequestMapping("/admin/to/login/page.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session){
//        调用service进行登陆检查
//        如果返回admin，登陆成功。密码不正确抛出异常
        Admin admin = adminService.getAdminByLogonAcct(loginAcct, userPswd);

//        将admin放入session
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main/page.html";
    }
    /*
    * 退出登陆
    * */
    @RequestMapping("/security/do/logout.html")
    public String doLogout(HttpSession httpSession){
//        强制session失效
        httpSession.invalidate();
        return "redirect:/admin/to/login/login.html";
    }
    /*
    * 获取页码
    * */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            // 使用defaultValue指定默认值
            @RequestParam(value = "keyword",defaultValue = "")String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            ModelMap modelmap
    ){
//            调用service方法获取pageInfo对象
        PageInfo<Admin> pageInfo = adminService.getInfo(keyword, pageNum, pageSize);
//            将pageInfo对象存入model模型
        modelmap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);

        return "admin-page";
    }
    /*
    *  删除用户
    * */
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String removeAdmin(
            @PathVariable("adminId")Integer adminId,
            @PathVariable("pageNum")Integer pageNum,
            @PathVariable("keyword")String keyword
    ){
        adminService.removeAdmin(adminId);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
    /*
    *  添加管理员用户
    * */
    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/do/add.html")
    public String add(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }
    /*
    *  根据id查询admin信息
    * */
    @RequestMapping("/admin/to/edit.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap){
//          根据adminId查询Admin对象
        Admin admin = adminService.getAdminById(adminId);
        //        将查出的admin放入modelmap，回显页面
        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }
    /*
    *  更新管理员信息
    * */
    @RequestMapping("/admin/update.html")
    public String update(
            Admin admin,
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("keyword")String keyword
    ){
        adminService.update(admin);


        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
}
