package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleHandler {
    @Autowired
    RoleService roleService;

//    角色分页
    @PreAuthorize("hasRole('部长')")
    @ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword",defaultValue = "") String keyword
    ){
//        调用service方法获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageSize, pageNum, keyword);
//        封装到ResultEntity对象中返回（如果上面操作出现异常，交给异常映射机制处理）
        return ResultEntity.successWithData(pageInfo);
    }
//    添加角色
    @ResponseBody
    @RequestMapping("role/save.json")
    public ResultEntity<String> saveRole(@RequestParam("roleName") String roleName){
        roleService.saveRole(new Role(null,roleName));
        return ResultEntity.successWithoutData();
    }
//    角色更新
    @ResponseBody
    @RequestMapping("role/update.json")
    public ResultEntity<String> updateRole(Role role){
//        执行更新方法
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }
//    删除角色
    @ResponseBody
    @RequestMapping("role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdArray(
            @RequestBody List<Integer> roleIdList){
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }
}
