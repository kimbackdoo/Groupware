package kr.co.metasoft.groupware.common.filter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kr.co.metasoft.groupware.api.common.dto.MenuParamDto;
import kr.co.metasoft.groupware.api.common.dto.RoleParamDto;
import kr.co.metasoft.groupware.api.common.entity.MenuEntity;
import kr.co.metasoft.groupware.api.common.entity.PersonEntity;
import kr.co.metasoft.groupware.api.common.entity.RoleEntity;
import kr.co.metasoft.groupware.api.common.entity.UserEntity;
import kr.co.metasoft.groupware.api.common.entity.UserPersonEntity;
import kr.co.metasoft.groupware.api.common.service.MenuService;
import kr.co.metasoft.groupware.api.common.service.RoleService;
import kr.co.metasoft.groupware.api.common.service.UserPersonService;
import kr.co.metasoft.groupware.api.common.service.UserService;
import kr.co.metasoft.groupware.common.exception.IsNotPemitException;
import kr.co.metasoft.groupware.common.keyvalue.entity.JwtKeyValueEntity;
import kr.co.metasoft.groupware.common.keyvalue.service.JwtKeyValueService;
import kr.co.metasoft.groupware.common.property.GroupwareProperty;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuDto;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuParamDto;
import kr.co.metasoft.groupware.common.util.menu.service.ApiUtilMenuService;

public class ImplUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) {
        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        Authentication authentication = null;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        String username = null;
        String password = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, Object>>() {});
            username = (String) map.get("username");
            password = (String) map.get("password");
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    AuthorityUtils.NO_AUTHORITIES);
        } catch (IOException e) {
        }
        try {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            if(webApplicationContext != null) {

                if(this.beforeAuthenticationProcessLockedException(username, webApplicationContext))
                    throw new LockedException("");

                if(this.beforeAuthenticationProcessIsNotPemitException(username, webApplicationContext))
                    throw new IsNotPemitException("");
            }
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (LockedException le) {
            this.isLockedException(response);
        } catch (IsNotPemitException iNP) {
            this.isIsNotPemitException(response);
        } catch (AuthenticationException ae) {
            this.isAuthenticationException(request,response,username);
        }
        return authentication;
    }

    public boolean beforeAuthenticationProcessLockedException (
            String username,
            WebApplicationContext webApplicationContext) {

        boolean isThrowsExepction = false;
        PageRequest pageRequest = new PageRequest();
        List<String> sort = new ArrayList<>();
        sort.add("created_date,desc");
        pageRequest.setSort(sort);
        pageRequest.setRowSize(5);
        return isThrowsExepction;
    }

    public boolean beforeAuthenticationProcessIsNotPemitException(
            String username,
            WebApplicationContext webApplicationContext) {
           boolean isThrowsExepction = false;
        UserService userService = webApplicationContext.getBean(UserService.class);
        UserEntity userEntity = userService.getUserByUsername(username != null ? username : "");

        if(userEntity != null && userEntity.getStatus() != null && !userEntity.getStatus().equals("") && userEntity.getStatus().equals("F"))
        {
            isThrowsExepction = true;
        }

        return isThrowsExepction;
    }

    public void occurExceptionSetResponse(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    public Map<String, Object> setExceptionMessage(String code,String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        return map;
    }

    public void isLockedException(
            HttpServletResponse response) {
        this.occurExceptionSetResponse(response);
        try (Writer writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            writer.write(objectMapper.writeValueAsString(this.setExceptionMessage("LOCKED", "잠긴 계정입니다.\n관리자에게 문의해주세요.")));
        } catch (IOException ioe) {
        }

    }

    public void isIsNotPemitException(
            HttpServletResponse response) {
        this.occurExceptionSetResponse(response);
        try (Writer writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            writer.write(objectMapper.writeValueAsString(this.setExceptionMessage("NotPermit", "승인되지 않은 계정입니다.\n관리자에게 문의해주세요.")));
        } catch (IOException ioe) {
        }
    }


    public void isAuthenticationException(
            HttpServletRequest request,
            HttpServletResponse response,
            String username) {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if(webApplicationContext != null) {
        }
        this.occurExceptionSetResponse(response);
        try (Writer writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            writer.write(objectMapper.writeValueAsString(this.setExceptionMessage("FAIL", "아이디 혹은 비밀번호가 틀렸습니다.")));
        } catch (IOException ioe) {
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
                    throws
                        IOException,
                        ServletException {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (webApplicationContext != null) {
            GroupwareProperty property = webApplicationContext.getBean(GroupwareProperty.class);
            JwtKeyValueService jwtKeyValueService = webApplicationContext.getBean(JwtKeyValueService.class);
            UserService userService = webApplicationContext.getBean(UserService.class);
            UserPersonService userPersonService = webApplicationContext.getBean(UserPersonService.class);
            MenuService menuService = webApplicationContext.getBean(MenuService.class);
            RoleService roleService = webApplicationContext.getBean(RoleService.class);
            ApiUtilMenuService apiUtilMenuService = webApplicationContext.getBean(ApiUtilMenuService.class);
            String username = authResult.getName();
            JwtKeyValueEntity jwtKeyValueEntity = null;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPage(1);
            pageRequest.setRowSize(100000000);
            PageRequest treeMenuDtoListPageRequest = new PageRequest();
            treeMenuDtoListPageRequest.setPage(1);
            treeMenuDtoListPageRequest.setRowSize(100000000);
            treeMenuDtoListPageRequest.setSort(Arrays.asList("rankingPath,asc"));
            UserEntity userEntity = userService.getUserByUsername(username);
            UserPersonEntity userPersonEntity = userPersonService.getUserPerson(userEntity.getId());
            PersonEntity personEntity = userPersonEntity != null ? userPersonEntity.getPerson() : null;
            List<RoleEntity> roleEntityList = roleService.getRoleList(RoleParamDto.builder().userId(userEntity.getId()).build(), pageRequest).getItems();
            List<MenuEntity> menuEntityList = menuService.getMenuList(MenuParamDto.builder().userId(userEntity.getId()).build(), pageRequest).getItems();
            List<TreeMenuDto> treeMenuDtoList = apiUtilMenuService.getTreeMenuList(TreeMenuParamDto.builder().userId(userEntity.getId()).build(), treeMenuDtoListPageRequest).getItems();
            Map<String, Object> userMap = objectMapper.convertValue(userEntity, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> personMap = objectMapper.convertValue(personEntity, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> roleList = objectMapper.convertValue(roleEntityList, new TypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> menuList = objectMapper.convertValue(menuEntityList, new TypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> treeMenuList = objectMapper.convertValue(treeMenuDtoList, new TypeReference<List<Map<String, Object>>>() {});
            userMap.remove("password");
            String secretKey = property.getJwt().getSecretKey();
            String subject = property.getJwt().getSubject();
            Long currentTimeMillis = System.currentTimeMillis();
            Long expirationTimeMillis = property.getJwt().getExpirationTimeMillis();
            Date currentTime = new Date(currentTimeMillis);
            Date expirationTime = new Date(currentTimeMillis + expirationTimeMillis);
            String token = null;
            if (roleList.isEmpty()) {
                throw new ServletException("권한 목록이 없습니다.");
            }
            try {
                JWSSigner jwsSigner = new MACSigner(secretKey);
                JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                        .subject(subject)
                        .issueTime(currentTime)
                        .expirationTime(expirationTime)
                        .notBeforeTime(currentTime)
                        .claim("user", userMap)
                        .claim("person", personMap)
                        .claim("roleList", roleList)
                        .claim("menuList", menuList)
                        .claim("treeMenuList", treeMenuList)
                        .build();
                SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet);
                signedJwt.sign(jwsSigner);
                token = signedJwt.serialize();
                jwtKeyValueEntity = JwtKeyValueEntity.builder()
                        .token(token)
                        .username(username)
                        .build();
                jwtKeyValueService.createJwtKeyValue(jwtKeyValueEntity);
            } catch (JOSEException e) {
            }
            response.setHeader("Authorization", "Bearer " + token);
        }
    }

}