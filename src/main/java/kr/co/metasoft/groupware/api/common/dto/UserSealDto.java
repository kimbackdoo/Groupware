package kr.co.metasoft.groupware.api.common.dto;

import java.sql.Blob;
import java.time.LocalDate;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

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

    private String sealName;

    private String signName;

    private MultipartFile sealMultiFile;

    private MultipartFile signMultiFile;

    private Blob sealImage;

    private Blob signImage;

}
