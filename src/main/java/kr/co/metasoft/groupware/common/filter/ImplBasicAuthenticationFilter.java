package kr.co.metasoft.groupware.common.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kr.co.metasoft.groupware.api.common.entity.MenuEntity;
import kr.co.metasoft.groupware.api.common.entity.PersonEntity;
import kr.co.metasoft.groupware.api.common.entity.RoleEntity;
import kr.co.metasoft.groupware.api.common.entity.UserEntity;
import kr.co.metasoft.groupware.common.keyvalue.entity.JwtKeyValueEntity;
import kr.co.metasoft.groupware.common.keyvalue.service.JwtKeyValueService;
import kr.co.metasoft.groupware.common.property.GroupwareProperty;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuDto;

public class ImplBasicAuthenticationFilter extends BasicAuthenticationFilter {

    public ImplBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
                    throws
                        IOException,
                        ServletException {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (webApplicationContext != null) {
            GroupwareProperty property = webApplicationContext.getBean(GroupwareProperty.class);
            JwtKeyValueService jwtKeyValueService = webApplicationContext.getBean(JwtKeyValueService.class);
            String secretKey = property.getJwt().getSecretKey();
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                String token = authorization.replace("Bearer ", "");
                JwtKeyValueEntity jwtKeyValueEntity = jwtKeyValueService.getJwtKeyValue(token);
                if (jwtKeyValueEntity != null) {
                    try {
                        JWSVerifier jwsVerifier = new MACVerifier(secretKey);
                        SignedJWT signedJwt = SignedJWT.parse(token);
                        if (signedJwt.verify(jwsVerifier)) {
                            JWTClaimsSet jwtClaimsSet = signedJwt.getJWTClaimsSet();
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.registerModule(new JavaTimeModule());
                            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                            Map<String, Object> userMap = objectMapper.convertValue(jwtClaimsSet.getClaim("user"), new TypeReference<Map<String, Object>>() {});
                            Map<String, Object> personMap = objectMapper.convertValue(jwtClaimsSet.getClaim("person"), new TypeReference<Map<String, Object>>() {});
                            List<Map<String, Object>> roleList = objectMapper.convertValue(jwtClaimsSet.getClaim("roleList"), new TypeReference<List<Map<String, Object>>>() {});
                            List<Map<String, Object>> menuList = objectMapper.convertValue(jwtClaimsSet.getClaim("menuList"), new TypeReference<List<Map<String, Object>>>() {});
                            List<Map<String, Object>> treeMenuList = objectMapper.convertValue(jwtClaimsSet.getClaim("treeMenuList"), new TypeReference<List<Map<String, Object>>>() {});
                            UserEntity userEntity = objectMapper.convertValue(userMap, UserEntity.class);
                            PersonEntity personEntity = objectMapper.convertValue(personMap, PersonEntity.class);
                            List<RoleEntity> roleEntityList = objectMapper.convertValue(roleList, new TypeReference<List<RoleEntity>>() {});
                            List<MenuEntity> menuEntityList = objectMapper.convertValue(menuList, new TypeReference<List<MenuEntity>>() {});
                            List<TreeMenuDto> treeMenuDtoList = objectMapper.convertValue(treeMenuList, new TypeReference<List<TreeMenuDto>>() {});
                            List<String> authorityList = new ArrayList<>();
                            for (int i = 0; i < roleEntityList.size(); i++) {
                                RoleEntity roleEntity = roleEntityList.get(i);
                                String value = roleEntity.getValue();
                                authorityList.add(value);
                            }
                            String username = userEntity.getUsername();
                            Map<String, Object> details = new HashMap<>();
                            details.put("user", userEntity);
                            details.put("person", personEntity);
                            details.put("roleList", roleEntityList);
                            details.put("menuList", menuEntityList);
                            details.put("treeMenuList", treeMenuDtoList);
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    username,
                                    token,
                                    AuthorityUtils.createAuthorityList(authorityList.toArray(new String[0])));
                            usernamePasswordAuthenticationToken.setDetails(details);
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    } catch (JOSEException | ParseException e) {
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

}