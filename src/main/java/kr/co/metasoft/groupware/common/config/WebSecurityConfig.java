package kr.co.metasoft.groupware.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import kr.co.metasoft.groupware.api.common.dto.ApiParamDto;
import kr.co.metasoft.groupware.api.common.dto.RoleApiParamDto;
import kr.co.metasoft.groupware.api.common.entity.ApiEntity;
import kr.co.metasoft.groupware.api.common.entity.RoleApiEntity;
import kr.co.metasoft.groupware.api.common.entity.RoleEntity;
import kr.co.metasoft.groupware.api.common.service.ApiService;
import kr.co.metasoft.groupware.api.common.service.RoleApiService;
import kr.co.metasoft.groupware.common.filter.ImplBasicAuthenticationFilter;
import kr.co.metasoft.groupware.common.filter.ImplLogoutFilter;
import kr.co.metasoft.groupware.common.filter.ImplUsernamePasswordAuthenticationFilter;
import kr.co.metasoft.groupware.common.keyvalue.service.JwtKeyValueService;
import kr.co.metasoft.groupware.common.util.PageRequest;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtKeyValueService jwtKeyValueService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private RoleApiService roleApiService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setRowSize(100000000);
        List<ApiEntity> apiEntityList = apiService.getApiList(new ApiParamDto(), pageRequest).getItems();
        for (int i = 0; i < apiEntityList.size(); i++) {
            ApiEntity apiEntity = apiEntityList.get(i);
            Long apiId = apiEntity.getId();
            String url = apiEntity.getUrl();
            String method = apiEntity.getMethod();
            List<String> roleValueList = new ArrayList<>();
            List<RoleApiEntity> roleApiEntityList = roleApiService.getRoleApiList(RoleApiParamDto.builder().apiId(apiId).build(), pageRequest).getItems();
            for (int j = 0; j < roleApiEntityList.size(); j++) {
                RoleApiEntity roleApiEntity = roleApiEntityList.get(j);
                RoleEntity roleEntity = roleApiEntity.getRole();
                String value = roleEntity.getValue();
                roleValueList.add(value);
            }
            String[] authorities = roleValueList.toArray(new String[0]);
            if ("ALL".equals(method)) {
                http.authorizeRequests().antMatchers(url).hasAnyAuthority(authorities);
            } else {
                http.authorizeRequests().antMatchers(HttpMethod.valueOf(method), url).hasAnyAuthority(authorities);
            }
        }
        http
            .cors().disable()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/api/login",
                        "/api/logout").permitAll()
                .antMatchers("/api").authenticated()
                .anyRequest().permitAll()
                .and()
            .addFilter(implUsernamePasswordAuthenticationFilter())
            .addFilter(implBasicAuthenticationFilter())
            .addFilter(implLogoutFilter())
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers("/api/app/accounts/sign-up",
                    "/api/common/users/id-exists");
    }


    private ImplUsernamePasswordAuthenticationFilter implUsernamePasswordAuthenticationFilter() throws Exception {
        ImplUsernamePasswordAuthenticationFilter implUsernamePasswordAuthenticationFilter =
                new ImplUsernamePasswordAuthenticationFilter();
        implUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        implUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/api/login");
        implUsernamePasswordAuthenticationFilter.setPostOnly(true);
        return implUsernamePasswordAuthenticationFilter;
    }

    private ImplBasicAuthenticationFilter implBasicAuthenticationFilter() throws Exception {
        return new ImplBasicAuthenticationFilter(authenticationManager());
    }

    private ImplLogoutFilter implLogoutFilter() {
        LogoutSuccessHandler logoutSuccessHandler = logoutSuccessHandler();
        LogoutHandler logoutHandler = logoutHandler();
        LogoutHandler[] logoutHandlers = {logoutHandler};
        ImplLogoutFilter implLogoutFilter = new ImplLogoutFilter(logoutSuccessHandler, logoutHandlers);
        implLogoutFilter.setFilterProcessesUrl("/api/logout");
        return implLogoutFilter;
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {};
    }

    private LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                String token = authorization.replace("Bearer ", "");
                jwtKeyValueService.removeJwtKeyValue(token);
            }
        };
    }

}