package kr.co.metasoft.groupware.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.api.common.dto.PersonParamDto;
import kr.co.metasoft.groupware.api.common.entity.PersonEntity;
import kr.co.metasoft.groupware.common.util.PageRequest;

@Repository
@Mapper
public interface PersonMapper {

    public List<PersonEntity> selectPersonList(
            @Param (value = "personParamDto") PersonParamDto personParamDto,
            @Param (value = "pageRequest") PageRequest pageRequest);

    public Integer selectPersonListCount(
            @Param (value = "personParamDto") PersonParamDto personParamDto);

    public PersonEntity selectPerson(
            @Param (value = "id") Long id);

}