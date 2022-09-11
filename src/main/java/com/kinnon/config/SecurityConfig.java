package com.kinnon.config;

import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kinnon
 * @create 2022-08-25 15:29
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements NewCoderConstant {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like/**",
                        "/follow/**",
                        "/unfollow/**")
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**"
                )
                .hasAnyAuthority(
                        AUTHORITY_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();


        //权限不足
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    //没有登录
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xrequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xrequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(NewCoderUtil.getJSONString(403,"你还没有登录"));
                        }else{
                            response.sendRedirect(request.getContextPath() + "/login");
                        }

                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    //权限不足
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xrequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xrequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(NewCoderUtil.getJSONString(404,"你没有访问该功能的权限"));
                        }else{
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        http.logout().logoutUrl("/securtylogout");


    }
}
