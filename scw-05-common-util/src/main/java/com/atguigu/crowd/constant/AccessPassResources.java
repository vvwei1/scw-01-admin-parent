package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/*
*   不需要登录检查的资源
* */
public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<>();
//    静态代码块：初始化静态变量（PASS_RES_SET）
    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
}
    public static final Set<String> STATIC_RES_SET = new HashSet<>();
    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }
    /*
    *  判断某个servletPath值是否对应一个静态资源
    * */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath){
//        1. 排除字符串无效情况
        if(servletPath == null || servletPath.length()==0){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
//        2. 切分servletPath字符串：/aa/bb/cc切分后结果: " ","aa","bb","cc"，所以取[1]
        String[] split = servletPath.split("/");
        String firstLevelPath = split[1];
//        3. 判断是否在集合中
        boolean contains = STATIC_RES_SET.contains(firstLevelPath);
        return contains;
    }
//    public static void main(String args[]){
//        String serPath = "/layer/bb/ede";
//        boolean b = AccessPassResources.judgeCurrentServletPathWhetherStaticResource(serPath);
//        System.out.println(b);
//    }
}
