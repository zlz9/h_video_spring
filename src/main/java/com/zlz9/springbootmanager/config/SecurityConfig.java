package com.zlz9.springbootmanager.config;



import com.zlz9.springbootmanager.filter.CaptchaFilter;
import com.zlz9.springbootmanager.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    CaptchaFilter captchaFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final String[] URL_WHITELIST = {
            "/swagger-resources/**",
            "/doc.html",
            "/webjars/**",
            "/v2/**",
            "/swagger-ui.html",
            "/api/captcha",
            "/api/login",
            "/api/logout",
            "/api/swiper/*",
            "/api/tag/*",
            "/api/video/*",
            "/api/top/*",
            "/api/register",
            "/api/sendEmail"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers(URL_WHITELIST)
                .permitAll()
//                .anonymous()
                .anyRequest().authenticated();
        //把token校验过滤器添加到过滤器链
        http.addFilterBefore(jwtAuthenticationTokenFilter,UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
//        允许跨域
        http.cors();

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).
                accessDeniedHandler(accessDeniedHandler);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

