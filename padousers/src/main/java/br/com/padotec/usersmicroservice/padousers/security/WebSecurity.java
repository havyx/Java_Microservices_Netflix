package br.com.padotec.usersmicroservice.padousers.security;

import br.com.padotec.usersmicroservice.padousers.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private UsersService usersService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(Environment environment, UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.environment = environment;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.usersService = usersService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //Bacause we are using JWT;
        http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip")) //TEMPORALLY
        .and().addFilter(getAuthenticationFilter());
        http.headers().frameOptions().disable(); //FOR H2 works here.
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());
    return authenticationFilter;
    }

    //Para spring saber qual servidor sera usado para carregar user details
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }

}
