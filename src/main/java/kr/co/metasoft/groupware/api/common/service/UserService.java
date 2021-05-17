package kr.co.metasoft.groupware.api.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.co.metasoft.groupware.api.common.dto.UserParamDto;
import kr.co.metasoft.groupware.api.common.entity.UserEntity;
import kr.co.metasoft.groupware.api.common.entity.UserPersonEntity;
import kr.co.metasoft.groupware.api.common.mapper.UserMapper;
import kr.co.metasoft.groupware.api.common.mapper.UserPersonMapper;
import kr.co.metasoft.groupware.api.common.repository.UserRepository;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPersonMapper userPersonMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PageResponse<UserEntity> getUserList(
            @Valid UserParamDto userParamDto,
            PageRequest pageRequest) {
        Integer userListCount = userMapper.selectUserListCount(userParamDto);
        List<UserEntity> userList = userMapper.selectUserList(userParamDto, pageRequest);
        PageResponse<UserEntity> pageResponse = new PageResponse<>(pageRequest, userListCount);
        pageResponse.setItems(userList);
        return pageResponse;
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public Integer getUserExists(
            @Valid UserParamDto userParamDto) {
        return userMapper.selectUserListCount(userParamDto);
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public UserEntity getUser(
            @Valid @NotNull (groups = {ReadValidationGroup.class}) Long id) {
        return userMapper.selectUser(id);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public List<UserEntity> createUserList(
            @Valid @NotEmpty (groups = {CreateValidationGroup.class}) List<@NotNull (groups = {CreateValidationGroup.class}) UserEntity> userList) {
        return userRepository.saveAll(userList);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public UserEntity createUser(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public List<UserEntity> modifyUserList(
            @Valid @NotEmpty (groups = {ModifyValidationGroup.class}) List<@NotNull (groups = {ModifyValidationGroup.class}) UserEntity> userList) {
        return userRepository.saveAll(userList);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public UserEntity modifyUser(
            @Valid @NotNull (groups = {ModifyValidationGroup.class}) UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeUserList(
            @Valid @NotEmpty (groups = {RemoveValidationGroup.class}) List<@NotNull (groups = {RemoveValidationGroup.class}) Long> idList) {
        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            userList.add(UserEntity.builder().id(id).build());
        }
        userRepository.deleteAll(userList);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeUser(
            @Valid @NotNull (groups = {RemoveValidationGroup.class}) Long id) {
        userRepository.delete(UserEntity.builder().id(id).build());
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public UserEntity getUserByUsername(
            @Valid @NotNull (groups = {ReadValidationGroup.class}) String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public void modifyPassword(Long id, String oldPassword, String newPassword) throws ServletException {
        UserPersonEntity userPersonEntity = userPersonMapper.selectUserPerson(id);
        String orignPassword = userPersonEntity.getUser().getPassword();
        if (passwordEncoder.matches(oldPassword, orignPassword)) {
            if (oldPassword.equals(newPassword)) {
                throw new ServletException("같은 비밀번호로는 변경이 불가능합니다.");
            } else {
                userPersonEntity.getUser().setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(userPersonEntity.getUser());
            }
        } else {
            throw new ServletException("현재 비밀번호가 다릅니다.");
        }
    }
    
    

}