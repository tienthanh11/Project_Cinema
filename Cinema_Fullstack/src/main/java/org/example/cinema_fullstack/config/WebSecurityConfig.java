package org.example.cinema_fullstack.config;

import org.example.cinema_fullstack.services.Impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Tắt CSRF nếu không cần thiết (chỉ nên tắt khi sử dụng API không cần bảo vệ CSRF)
        http.csrf().disable();

        // Cấu hình cho trang homepages có thể truy cập công khai
        http.authorizeRequests()
                .antMatchers("/**", "/login", "/book/**", "/paypal/**").permitAll()  // Cho phép trang homepages và login truy cập công khai
                .antMatchers("/member/**", "/book/member/**").access("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/deny");

        // Cấu hình đăng nhập
        http.formLogin()
                .loginPage("/login") // trang đăng nhập
                .loginProcessingUrl("/doLogin") // URL xử lý đăng nhập
                .defaultSuccessUrl("/", true) // URL sau khi đăng nhập thành công
                .failureUrl("/login?error=true") // URL nếu đăng nhập thất bại
                .usernameParameter("username") // tham số tên người dùng
                .passwordParameter("password") // tham số mật khẩu
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL để logout
                .logoutSuccessUrl("/") // URL sau khi logout thành công
                .deleteCookies("JSESSIONID") // Xoá cookie phiên
                .invalidateHttpSession(true);

        // Cấu hình "Remember Me"
        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 1); // 1 ngày
        }
}


