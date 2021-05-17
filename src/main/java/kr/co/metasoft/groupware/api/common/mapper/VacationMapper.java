package kr.co.metasoft.groupware.api.common.mapper;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.api.common.dto.VacationParamDto;
import kr.co.metasoft.groupware.api.common.entity.VacationEntity;
import kr.co.metasoft.groupware.common.util.PageRequest;

@Mapper
@Repository
public interface VacationMapper {

    public VacationEntity selectVacation(
            @Param (value = "id") Long id);

    public List<VacationEntity> selectVacationList(
            @Param (value = "vacationParamDto") VacationParamDto vacationParamDto,
            @Param (value = "pageRequest") PageRequest pageRequest);

    public Integer selectVacationListCount(
            @Param (value = "vacationParamDto") @Valid VacationParamDto vacationParamDto);



}
