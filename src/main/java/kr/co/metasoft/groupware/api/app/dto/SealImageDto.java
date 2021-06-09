package kr.co.metasoft.groupware.api.app.dto;

import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

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
public class SealImageDto {

    @Transient
    private MultipartFile sealImage;

    @Transient
    private MultipartFile signImage;
}
