package kr.co.metasoft.groupware.api.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.api.app.service.ExpenditureExcelService;

@RestController
@RequestMapping(path = "api/app/expenditureXlsx")
public class ApiAppExpenditureController {


    @Autowired
    private ExpenditureExcelService expenditureExcelService;

    @GetMapping(path = "")
    public byte[] downloadExpenditureXlsx() throws IOException {
        return expenditureExcelService.getExpenditureExcel();
    }

}
