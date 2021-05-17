package kr.co.metasoft.groupware.api.app.dto;

import java.util.List;

import kr.co.metasoft.groupware.api.common.dto.PersonDto;
import kr.co.metasoft.groupware.api.common.dto.UserDto;
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
public class AccountDto {

    private Long userId;

    private Long personId;

    private List<Long> roleId;
    
    private UserDto userDto;

    private PersonDto personDto;
    
}
