package kr.co.metasoft.groupware.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.co.metasoft.groupware.api.common.dto.CodeParamDto;
import kr.co.metasoft.groupware.api.common.entity.CodeEntity;
import kr.co.metasoft.groupware.api.common.mapper.CodeMapper;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;


@Service
public class CodeService {

    @Autowired
    private CodeMapper codeMapper;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
   public PageResponse<CodeEntity> getCodeList(
           @Valid CodeParamDto codeParamDto,
           PageRequest pageRequest) {
        Integer codeListCount = codeMapper.selectCodeListCount(codeParamDto);
        List<CodeEntity> codeList = codeMapper.selectCodeList(codeParamDto, pageRequest);
        PageResponse<CodeEntity> pageResponse = new PageResponse<>(pageRequest, codeListCount);
        pageResponse.setItems(codeList);
        return pageResponse;
   }


    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public CodeEntity getCode(
            @Valid @NotNull (groups = {ReadValidationGroup.class}) Long id) {
        return codeMapper.selectCode(id);
    }
}
