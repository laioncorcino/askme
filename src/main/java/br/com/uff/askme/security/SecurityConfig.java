package br.com.uff.askme.security;

import br.com.uff.askme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/topic")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/topic/**")
                .permitAll()
            .antMatchers(HttpMethod.POST, "/user/signup")
                .permitAll()
            .antMatchers(HttpMethod.POST, "/login")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/user")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/user/**")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/actuator/**")
                .permitAll()
            .antMatchers(HttpMethod.DELETE, "/topic/*")
                .hasRole("ADMINISTRATOR")
            .antMatchers(HttpMethod.PUT, "/user/profile/**")
                .hasRole("ADMINISTRATOR")
            .anyRequest()
                .authenticated()
                .and()
                    .csrf()
                        .disable()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilterBefore(new AuthenticationTokenFilter(tokenService, userService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }

}
