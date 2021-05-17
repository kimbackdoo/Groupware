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

import kr.co.metasoft.groupware.api.common.dto.MenuParamDto;
import kr.co.metasoft.groupware.api.common.entity.MenuEntity;
import kr.co.metasoft.groupware.api.common.mapper.MenuMapper;
import kr.co.metasoft.groupware.api.common.repository.MenuRepository;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PageResponse<MenuEntity> getMenuList(
            @Valid MenuParamDto menuParamDto,
            PageRequest pageRequest) {
        Integer menuListCount = menuMapper.selectMenuListCount(menuParamDto);
        List<MenuEntity> menuList = menuMapper.selectMenuList(menuParamDto, pageRequest);
        PageResponse<MenuEntity> pageResponse = new PageResponse<>(pageRequest, menuListCount);
        pageResponse.setItems(menuList);
        return pageResponse;
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public MenuEntity getMenu(
            @Valid @NotNull (groups = {ReadValidationGroup.class}) Long id) {
        return menuMapper.selectMenu(id);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public List<MenuEntity> createMenuList(
            @Valid @NotEmpty (groups = {CreateValidationGroup.class}) List<@NotNull (groups = {CreateValidationGroup.class}) MenuEntity> menuList) {
        return menuRepository.saveAll(menuList);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public MenuEntity createMenu(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) MenuEntity menuEntity) {
        return menuRepository.save(menuEntity);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public List<MenuEntity> modifyMenuList(
            @Valid @NotEmpty (groups = {ModifyValidationGroup.class}) List<@NotNull (groups = {ModifyValidationGroup.class}) MenuEntity> menuList) {
        return menuRepository.saveAll(menuList);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public MenuEntity modifyMenu(
            @Valid @NotNull (groups = {ModifyValidationGroup.class}) MenuEntity menuEntity) {
        return menuRepository.save(menuEntity);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeMenuList(
            @Valid @NotEmpty (groups = {RemoveValidationGroup.class}) List<@NotNull (groups = {RemoveValidationGroup.class}) Long> idList) {
        List<MenuEntity> menuList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            menuList.add(MenuEntity.builder().id(id).build());
        }
        menuRepository.deleteAll(menuList);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removeMenu(
            @Valid @NotNull (groups = {RemoveValidationGroup.class}) Long id) {
        menuRepository.delete(MenuEntity.builder().id(id).build());
    }

}