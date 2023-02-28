package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {
    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLogonAcct(String username,String pwd);

    PageInfo<Admin> getInfo(String keyword,Integer pageNum,Integer pageSize);

    void removeAdmin(Integer adminId);

    void update(Admin admin);

    Admin getAdminById(Integer adminId);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String username);
}
