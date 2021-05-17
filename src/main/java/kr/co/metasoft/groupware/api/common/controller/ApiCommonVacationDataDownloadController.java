package kr.co.metasoft.groupware.api.common.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.api.common.dto.VacationParamDto;
import kr.co.metasoft.groupware.api.common.service.VacationDataDownloadService;
import kr.co.metasoft.groupware.common.util.PageRequest;

@RestController
@RequestMapping(path = "api/app/vacationDataXlsx")
public class ApiCommonVacationDataDownloadController {

    @Autowired
    private VacationDataDownloadService vacationDataDownloadService;

    @GetMapping(path = "")
    public byte[] getVacationDataXlsx(
            @ModelAttribute VacationParamDto vacationParamDto) throws IOException {
        return vacationDataDownloadService.getVacationXlsx(vacationParamDto);
    }


}
