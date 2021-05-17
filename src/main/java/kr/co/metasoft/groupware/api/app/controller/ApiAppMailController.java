package kr.co.metasoft.groupware.api.app.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.api.app.dto.MailDto;
import kr.co.metasoft.groupware.api.app.service.MailSendService;
import kr.co.metasoft.groupware.api.common.dto.VacationParamDto;

@RestController
@RequestMapping(path = "api/app/mails")
public class ApiAppMailController {


    @Autowired
    private MailSendService mailSendService;

    @GetMapping(path = "")
    public void getMailSend(
            @ModelAttribute MailDto mailDto) throws IOException, MessagingException {
        mailSendService.mailSend(mailDto);
    }


}
