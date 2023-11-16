package com.ep.weare.config;

import com.ep.weare.user.service.UserService;
import com.ep.weare.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserServiceImpl userService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * WebSecurityConfigurerAdapter가 Spring Security 5.7.0-M2부터 deprecated 됨.
     * component-based security configuration으로의 사용자 전환 격려 위함.
     * 따라서 아래와 같이 bean으로 등록하여 사용.
     */

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(
                        new AntPathRequestMatcher("/favicon.ico"),
                        new AntPathRequestMatcher("/resources/**"),
                        new AntPathRequestMatcher("/error")
                );
    };


    /**
     * 요청별 인증여부/권한여부 설정
     * - permitAll 전체허용
     * - authenticated 인증된 사용자만 허용
     * - anonymous 인증안된 사용자만 허용
     * - hasRole
     * - hasAuthority
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(
                        new AntPathRequestMatcher("/assets/**"),
                        new AntPathRequestMatcher("/img/**"),
                        new AntPathRequestMatcher("/home"),
                        new AntPathRequestMatcher("/signup/**"),
                        new AntPathRequestMatcher("/signupComplete")
                ).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/leader/**")
                ).hasRole("leader")
                .anyRequest().authenticated()
                );

        // /login
        http
                .formLogin(login -> login
                .loginPage("/login") // 로그인 폼페이지 작성필수
                .loginProcessingUrl("/userlogin.do")
                .usernameParameter("username")
                .passwordParameter("userpw")
                .failureUrl("/loginFail")
                .defaultSuccessUrl("/home")
                .permitAll()
                );

        http
                .logout(logout -> logout
                .logoutUrl("/logout.do")
                .logoutSuccessUrl("/home")
                .permitAll()
                );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
