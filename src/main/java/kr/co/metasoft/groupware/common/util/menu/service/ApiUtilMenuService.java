package kr.co.metasoft.groupware.common.util.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuDto;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuParamDto;
import kr.co.metasoft.groupware.common.util.menu.mapper.ApiUtilMenuMapper;

@Service
public class ApiUtilMenuService {

    @Autowired
    private ApiUtilMenuMapper apiUtilMenuMapper;

    @Transactional (readOnly = true)
    public PageResponse<TreeMenuDto> getTreeMenuList(
            TreeMenuParamDto treeMenuParamDto,
            PageRequest pageRequest) {
        Integer treeMenuListCount = apiUtilMenuMapper.selectTreeMenuListCount(treeMenuParamDto);
        List<TreeMenuDto> treeMenuList = apiUtilMenuMapper.selectTreeMenuList(treeMenuParamDto, pageRequest);
        PageResponse<TreeMenuDto> pageResponse = new PageResponse<>(pageRequest, treeMenuListCount);
        pageResponse.setItems(treeMenuList);
        return pageResponse;
    }

}