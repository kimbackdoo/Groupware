package kr.co.metasoft.groupware.api.common.dto;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPersonDto {

    private Long userId;

    private Long personId;

    @Transient
    private UserDto userDto;

    @Transient
    private PersonDto personDto;

}