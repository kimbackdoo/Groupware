package kr.co.metasoft.groupware.api.common.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.metasoft.groupware.api.common.entity.UserSealEntity;

@Mapper
@Repository
public interface UserSealMapper {


    public UserSealEntity selectUserSeal (
            @Param (value = "userId") Long userId);


}
