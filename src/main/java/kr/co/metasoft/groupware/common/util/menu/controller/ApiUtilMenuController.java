package kr.co.metasoft.groupware.common.util.menu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuDto;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuParamDto;
import kr.co.metasoft.groupware.common.util.menu.service.ApiUtilMenuService;

@RestController
@RequestMapping (path = "api/util/menus")
public class ApiUtilMenuController {

    @Autowired
    private ApiUtilMenuService apiUtilMenuService;

    @GetMapping (path = "tree-menus")
    public PageResponse<TreeMenuDto> getTreeMenuList(
            @ModelAttribute TreeMenuParamDto treeMenuParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return apiUtilMenuService.getTreeMenuList(treeMenuParamDto, pageRequest);
    }

}