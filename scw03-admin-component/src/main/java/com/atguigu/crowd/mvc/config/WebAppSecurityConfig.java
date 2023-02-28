package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

//  声明当前类为配置类
@Configuration
//  启用web环境权限控制功能
@EnableWebSecurity
//  启用全局方法权限控制功能，
//  并且设置 prePostEnabled = true。
//  保证@PreAuthority、@PostAuthority、 @PreFilter、 @PostFilter 生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    /*
    * 加密
    * */
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    /*
    * 与用户登陆相关
    * */
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

//        临时使用内存版登录模式，进行测试
//        builder
//                .inMemoryAuthentication()
//                .passwordEncoder(new BCryptPasswordEncoder()).withUser("kang").password(new BCryptPasswordEncoder().encode("123456")).roles("USER");

//        正式功能中使用基于数据库的认证
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


    /*
    * 与请求授权相关
    * */
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()
                .antMatchers("/admin/to/login/login.html")
//                对于登录页授权
                .permitAll()
//                可以无条件访问
                .antMatchers("/bootstrap/**")       // 针对静态资源进行设置， 无条件访问
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/crowd/**")
                .permitAll()
                .antMatchers("/layer/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()
                .antMatchers("/admin/get/page.html")
//                针对分页显示admin数据设定访问控制
                .access("hasRole('经理') OR hasAuthority('user:get')")
//                要求是经理角色或get权限
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, e) -> {
                    request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                    request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request, response);
                })
//                需要登录以后访问
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/admin/to/login/login.html")
                .loginProcessingUrl("/security/do/login.html")
//                指定处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html")         // 指定登录成功后前往的地址
                .usernameParameter("loginAcct")                 // 账号的请求参数名称
                .passwordParameter("userPswd")
                .and()
                .logout()                                       // 退出登录功能
                .logoutUrl("/security/do/logout.html")
                .logoutSuccessUrl("/security/do/login.html")
        ;
    }
}
