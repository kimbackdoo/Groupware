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

import kr.co.metasoft.groupware.api.common.dto.RoleParamDto;
import kr.co.metasoft.groupware.api.common.entity.RoleEntity;
import kr.co.metasoft.groupware.api.common.mapper.RoleMapper;
import kr.co.metasoft.groupware.api.common.repository.RoleRepository;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PageResponse<RoleEntity> getRoleList(
            @Valid RoleParamDto roleParamDto,
            PageRequest pageRequest) {
        Integer roleListCount = roleMapper.selectRoleListCount(roleParamDto);
        List<RoleEntity> roleList = roleMapper.selectRoleList(roleParamDto, pageRequest);
        PageResponse<RoleEntity> pageResponse = new PageResponse<>(pageRequest, roleListCount);
        pageResponse.setItems(roleList);
        return pageResponse;
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public RoleEntity getRole(
            @Valid @NotNull (groups = {ReadValidationGroup.class}) Long id) {
        return roleMapper.selectRole(id);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public List<RoleEntity> createRoleList(
            @Valid @NotEmpty (groups = {CreateValidationGroup.class}) List<@NotNull (groups = {CreateValidationGroup.class}) RoleEntity> roleList) {
        return roleRepository.saveAll(roleList);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public RoleEntity createRole(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public List<RoleEntity> modifyRoleList(
            @Valid @NotEmpty (groups = {ModifyValidationGroup.class}) List<@NotNull (groups = {ModifyValidationGroup.class}) RoleEntity> roleList) {
        return roleRepository.saveAll(roleList);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public RoleEntity modifyRole(
            @Valid @NotNull (groups = {ModifyValidationGroup.class}) RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeRoleList(
            @Valid @NotEmpty (groups = {RemoveValidationGroup.class}) List<@NotNull (groups = {RemoveValidationGroup.class}) Long> idList) {
        List<RoleEntity> roleList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            roleList.add(RoleEntity.builder().id(id).build());
        }
        roleRepository.deleteAll(roleList);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeRole(
            @Valid @NotNull (groups = {RemoveValidationGroup.class}) Long id) {
        roleRepository.delete(RoleEntity.builder().id(id).build());
    }

}