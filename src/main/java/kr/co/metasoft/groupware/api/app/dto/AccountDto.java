package kr.co.metasoft.groupware.api.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

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


    private String sealName;

    private String signName;

    private MultipartFile sealImage;

    private MultipartFile signImage;

    private Long userId;

    private Long personId;

    private List<Long> roleId;

    private UserDto userDto;

    //---------------userDto -----------------------------
    private String username;

    private String password;

    private String name;

    private String email;

    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String department;

    private String teamName;

    private String position;

    private String status;

    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate lastModifiedPasswordDate;
    //---------------userDto -----------------------------



    private PersonDto personDto;

}
