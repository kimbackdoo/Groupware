package kr.co.metasoft.groupware.api.common.mapper;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.api.common.dto.Notice2ParamDto;
import kr.co.metasoft.groupware.api.common.entity.NoticeEntity;
import kr.co.metasoft.groupware.common.util.PageRequest;

@Mapper
@Repository
public interface NoticeMapper {


    public List<NoticeEntity> selectNoticeList (
            @Param (value = "noticeParamDto") @Valid Notice2ParamDto noticeParamDto,
            @Param (value = "pageRequest") PageRequest pageRequest );

    public NoticeEntity selectNotice (
            @Param (value = "id") Long id);

    public Integer selectNoticeListCount(
            @Param (value = "noticeParamDto") @Valid Notice2ParamDto noticeParamDto);


}
