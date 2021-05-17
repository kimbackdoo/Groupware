package kr.co.metasoft.groupware.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.api.common.dto.CodeParamDto;
import kr.co.metasoft.groupware.api.common.entity.CodeEntity;
import kr.co.metasoft.groupware.api.common.service.CodeService;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;


@RestController
@RequestMapping (path = "api/common/codes")
public class ApiCommonCodeController {

    @Autowired
    private CodeService codeService;

    @GetMapping (path = "")
    public PageResponse<CodeEntity> getCodeList(
            @ModelAttribute CodeParamDto codeParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return codeService.getCodeList(codeParamDto, pageRequest);
    }

    @GetMapping (path = "{id}")
    public CodeEntity getCode(
            @PathVariable (name = "id") Long id) {
        return codeService.getCode(id);
    }

}
