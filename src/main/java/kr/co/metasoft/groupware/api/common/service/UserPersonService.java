package kr.co.metasoft.groupware.api.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.co.metasoft.groupware.api.common.dto.UserPersonParamDto;
import kr.co.metasoft.groupware.api.common.entity.UserPersonEntity;
import kr.co.metasoft.groupware.api.common.mapper.UserPersonMapper;
import kr.co.metasoft.groupware.api.common.repository.UserPersonRepository;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class UserPersonService {

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Autowired
    private UserPersonMapper userPersonMapper;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PageResponse<UserPersonEntity> getUserPersonList(
            @Valid UserPersonParamDto userPersonParamDto,
            PageRequest pageRequest) {
        Integer userPersonListCount = userPersonMapper.selectUserPersonListCount(userPersonParamDto);
        List<UserPersonEntity> userPersonList = userPersonMapper.selectUserPersonList(userPersonParamDto, pageRequest);
        PageResponse<UserPersonEntity> pageResponse = new PageResponse<>(pageRequest, userPersonListCount);
        pageResponse.setItems(userPersonList);
        return pageResponse;
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public UserPersonEntity getUserPerson(
            @Valid @NotNull (groups = {ReadValidationGroup.class}) Long userId) {
        return userPersonMapper.selectUserPerson(userId);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public List<UserPersonEntity> createUserPersonList(
            @Valid @NotEmpty (groups = {CreateValidationGroup.class}) List<@NotNull (groups = {CreateValidationGroup.class}) UserPersonEntity> userPersonList) {
        return userPersonRepository.saveAll(userPersonList);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public UserPersonEntity createUserPerson(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) UserPersonEntity userPersonEntity) {
        return userPersonRepository.save(userPersonEntity);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public List<UserPersonEntity> modifyUserPersonList(
            @Valid @NotEmpty (groups = {ModifyValidationGroup.class}) List<@NotNull (groups = {ModifyValidationGroup.class}) UserPersonEntity> userPersonList) {
        return userPersonRepository.saveAll(userPersonList);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public UserPersonEntity modifyUserPerson(
            @Valid @NotNull (groups = {ModifyValidationGroup.class}) UserPersonEntity userPersonEntity) {
        return userPersonRepository.save(userPersonEntity);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeUserPersonList(
            @Valid @NotEmpty (groups = {RemoveValidationGroup.class}) List<@NotNull (groups = {RemoveValidationGroup.class}) Long> userIdList) {
        List<UserPersonEntity> userPersonList = new ArrayList<>();
        for (int i = 0; i < userIdList.size(); i++) {
            Long userId = userIdList.get(i);
            userPersonList.add(UserPersonEntity.builder().userId(userId).build());
        }
        userPersonRepository.deleteAll(userPersonList);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeUserPerson(
            @Valid @NotNull (groups = {RemoveValidationGroup.class}) Long userId) {
        userPersonRepository.delete(UserPersonEntity.builder().userId(userId).build());
    }
}