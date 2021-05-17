package kr.co.metasoft.groupware.api.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.api.common.dto.VacationDto;
import kr.co.metasoft.groupware.api.common.dto.VacationParamDto;
import kr.co.metasoft.groupware.api.common.entity.VacationEntity;
import kr.co.metasoft.groupware.api.common.service.VacationService;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/app/vacations")
public class ApiCommonVacationController {

    @Autowired
    private VacationService vacationService;

    @GetMapping(path = "")
    public PageResponse<VacationEntity> getVacationList(
            @ModelAttribute VacationParamDto vacationParamDto,
            @ModelAttribute PageRequest pageRequest){
        return vacationService.getVacationList(vacationParamDto, pageRequest);
    }


    @GetMapping(path = "{id}")
    public VacationEntity getVacationInfo(
            @PathVariable (name = "id") Long id) {
        return vacationService.getVacation(id);
    }

    @PostMapping(path = "")
    public VacationEntity createVacation(
            @RequestBody VacationDto vacationDto) {

        VacationEntity vacationEntity = VacationEntity.builder()
                .userId(vacationDto.getUserId())
                .takingUser(vacationDto.getTakingUser())
                .department(vacationDto.getDepartment())
                .emergencyNum(vacationDto.getEmergencyNum())
                .type(vacationDto.getType())
                .sterm(vacationDto.getSterm())
                .eterm(vacationDto.getEterm())
                .detail(vacationDto.getDetail())
                .build();
        return vacationService.createVacation(vacationEntity);
    }

    @PutMapping(path = "{id}")
    public VacationEntity modifyVacation(
            @PathVariable (name = "id") Long id,
            @RequestBody VacationDto vacationDto) {

        VacationEntity vacationEntity = VacationEntity.builder()
                .id(id)
                .userId(vacationDto.getUserId())
                .takingUser(vacationDto.getTakingUser())
                .department(vacationDto.getDepartment())
                .emergencyNum(vacationDto.getEmergencyNum())
                .type(vacationDto.getType())
                .sterm(vacationDto.getSterm())
                .eterm(vacationDto.getEterm())
                .detail(vacationDto.getDetail())
                .build();
        return vacationService.modifyVacation(vacationEntity);
    }

    @DeleteMapping(path = "{id}")
    public void removeVacation(
            @PathVariable (name = "id") Long id) {
        vacationService.removeVacation(id);
    }

    @DeleteMapping(path = "")
    public void removeVacationList(
            @RequestBody List<Long> idList) {
        vacationService.removeVacationList(idList);
    }

}
