package kr.co.metasoft.groupware.api.app.dto;

import java.util.List;

import com.sun.istack.ByteArrayDataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.*;


@Getter
@Setter
@ToString
public class MailDto {

    //발신자
    private String from;

    //수신자
    private String to;

    //제목
    private String subject;

    //내용
    private String text;

    //파일이름
    private String fileName;

    private ByteArrayDataSource fileData;

}
