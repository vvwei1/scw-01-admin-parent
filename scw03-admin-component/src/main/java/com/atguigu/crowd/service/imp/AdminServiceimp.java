package com.atguigu.crowd.service.imp;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceimp implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminServiceimp.class);
   /*
   * 添加用户
   * */
    @Override
    public void saveAdmin(Admin admin) {
        // 密码加密
        String userPswd = admin.getUserPswd();
        admin.setUserPswd(passwordEncoder.encode(userPswd));

//        生成创建时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(date);
        admin.setCreateTime(createTime);
//        保存
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常全类名"+e.getClass().getName());
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        List<Admin> adminList = adminMapper.selectByExample(new AdminExample());
        return adminList;
    }

    @Override
    public Admin getAdminByLogonAcct(String loginAcct, String userPswd) {
//        adminMapper.selectByExample()
//        根据账号查找admin对象
        AdminExample adminExample = new AdminExample();
//        ①创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
//        ②封装查询条件
        criteria.andLoginEqualTo(loginAcct);
//        判断是否为null
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
//        如果null，抛出异常
        if (adminList == null || adminList.size() ==0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
//        如果list长度>1，数据库中的数据出现问题
        if (adminList.size()>1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = adminList.get(0);
//        为null，抛异常
        if (admin == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
//        如果结果不为null，将数据库密码从admin取出
        String userPswdDB = admin.getUserPswd();
//        将表单提交的明文密码加密
        String userPswmForm = CrowdUtil.md5(userPswd);

//        对密码进行比较
        if(!Objects.equals(userPswdDB, userPswmForm)){
//            不一致 抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
//        密码正确，返回admin对象
        return admin;
    }
/*
*   获取页码
* */
    @Override
    public PageInfo<Admin> getInfo(String keyword, Integer pageNum, Integer pageSize) {
//        1. 调用pageHelper的静态方法，开启分页功能
//        pageHelper的“非侵入式”，原本的查询无需任何修改
        PageHelper.startPage(pageNum, pageSize);
//        2. 执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

//        3. 封装到pageinfo对象中
        return new PageInfo<>(list);
    }

    /*
     *   删除用户
     * */
    @Override
    public void removeAdmin(Integer adminId) {

        adminMapper.deleteByPrimaryKey(adminId);
    }

    /*
    *    修改用户信息
    * */
    @Override
    public void update(Admin admin) {
//        selective: 有选择的更新、对于null字段，不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
//            用户名重复
            logger.info("异常全类名"+e.getClass().getName());
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
//        1. 先根据adminId删除旧的数据
        adminMapper.deleteOldRelationship(adminId);
//        2. 再根据adminId保存全部新的数据
        if (roleIdList != null && roleIdList.size()>0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample example = new AdminExample();

        AdminExample.Criteria criteria = example.createCriteria();

        criteria.andLoginEqualTo(username);

        return adminMapper.selectByExample(example).get(0);
    }


}
