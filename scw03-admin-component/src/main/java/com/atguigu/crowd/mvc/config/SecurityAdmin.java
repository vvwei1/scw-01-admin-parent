package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
/*
*  用户、角色、权限的集合类
* */
public class SecurityAdmin extends User {
    private static final long serialVersionUID = 1L;

    // 原始的 Admin 对象， 包含 Admin 对象的全部属性
    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin,
//                         创建角色、权限信息的集合
                         List<GrantedAuthority> authorities) {
        super(originalAdmin.getLogin(), originalAdmin.getUserPswd(), authorities);
//        给本类的originalAdmin赋值
        this.originalAdmin = originalAdmin;
//        擦除originalAdmin的密码
        this.originalAdmin.setUserPswd(null);
    }

    // 对外提供的获取原始 Admin 对象的 getXxx()方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
