package kr.co.metasoft.groupware.api.common.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.co.metasoft.groupware.api.common.entity.UserSealEntity;
import kr.co.metasoft.groupware.api.common.mapper.UserSealMapper;
import kr.co.metasoft.groupware.api.common.repository.UserSealRepository;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Service
public class UserSealService {

    @Autowired
    UserSealRepository userSealRepository;

    @Autowired
    UserSealMapper userSealMapper;


    @Validated( value = {CreateValidationGroup.class})
    @Transactional
    public UserSealEntity createUserSeal(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) UserSealEntity userSealEntity) {
        return userSealRepository.save(userSealEntity);
    }

    @Validated( value = {ModifyValidationGroup.class})
    @Transactional
    public UserSealEntity modifyUserSeal(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) UserSealEntity userSealEntity) {
        return userSealRepository.save(userSealEntity);
    }

    @Validated( value = {RemoveValidationGroup.class})
    @Transactional
    public void removeUserSeal(
         @Valid Long id) {
        userSealRepository.deleteById(id);
    }

    @Validated( value = {ReadValidationGroup.class})
    @Transactional
    public UserSealEntity selectUserseal(
            @Valid Long userId) {
        return userSealMapper.selectUserSeal(userId);
    }


}
