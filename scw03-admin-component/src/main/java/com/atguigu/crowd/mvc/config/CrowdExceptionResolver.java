package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.AccessForbidden;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//  @ControllerAdvice表示是一个异常处理器类
@ControllerAdvice
public class CrowdExceptionResolver {
//    将一个具体的异常类型和方法映射起来
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(NullPointerException exception
    , HttpServletRequest request
    , HttpServletResponse response) throws IOException {
            String viewName="system-error";
            return  commonResolve(viewName, exception, request, response);
    }
    // 未登录异常
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(Exception exception,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName,exception,request,response);
    }
//    登陆失败异常处理方法
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(NullPointerException exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String viewName = "admin-login";
        return commonResolve(viewName,exception,request,response);
    }
    // 未登录异常
    @ExceptionHandler(value = AccessForbidden.class)
    public ModelAndView resolveAccessForbiddenException(AccessForbidden exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName,exception,request,response);
    }
    // 添加时账号重复异常
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView LoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolve(viewName,exception,request,response);
    }
//    更新时账号重复异常
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(LoginAcctAlreadyInUseForUpdateException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName,exception,request,response);
}
//    抽取方法
    private ModelAndView commonResolve(String viewName
    ,Exception exception
    ,HttpServletRequest request
    ,HttpServletResponse response) throws IOException {
        boolean b = CrowdUtil.judgeRequestType(request);
//    如果是ajax请求
        if(b){
//            创建resultentity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
//            将resultEntity转为json字符串
            Gson gson = new Gson();
            String s = gson.toJson(resultEntity);
//            响应给浏览器
            response.getWriter().write(s);
            return null;
        }
//     如果不是ajax请求，返回modelandView对象
        ModelAndView modelAndView = new ModelAndView();
//        将exception存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
//        设置视图名称
        modelAndView.setViewName(viewName);
        return modelAndView;

    }
}
