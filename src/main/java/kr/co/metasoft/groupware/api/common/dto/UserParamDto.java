package kr.co.metasoft.groupware.api.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserParamDto {

    private String username;

    private String department;

    private String teamName;

    private String position;

}