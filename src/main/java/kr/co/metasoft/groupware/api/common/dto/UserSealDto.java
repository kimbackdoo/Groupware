package kr.co.metasoft.groupware.api.common.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSealDto {

    private Long id;

    private Long userId;

    private String imageUrl;

    private String signUrl;

}
