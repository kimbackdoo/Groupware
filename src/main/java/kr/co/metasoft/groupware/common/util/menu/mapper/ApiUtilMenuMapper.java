package kr.co.metasoft.groupware.common.util.menu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuDto;
import kr.co.metasoft.groupware.common.util.menu.dto.TreeMenuParamDto;

@Repository
@Mapper
public interface ApiUtilMenuMapper {

    public List<TreeMenuDto> selectTreeMenuList(
            @Param (value = "treeMenuParamDto") TreeMenuParamDto treeMenuParamDto,
            @Param (value = "pageRequest") PageRequest pageRequest);

    public Integer selectTreeMenuListCount(
            @Param (value = "treeMenuParamDto") TreeMenuParamDto treeMenuParamDto);

}