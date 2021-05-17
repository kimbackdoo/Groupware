package kr.co.metasoft.groupware.api.common.controller;

import java.util.ArrayList;
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

import kr.co.metasoft.groupware.api.common.dto.PersonDto;
import kr.co.metasoft.groupware.api.common.dto.PersonParamDto;
import kr.co.metasoft.groupware.api.common.entity.PersonEntity;
import kr.co.metasoft.groupware.api.common.service.PersonService;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;

@RestController
@RequestMapping (path = "api/common/people")
public class ApiCommonPersonController {

    @Autowired
    private PersonService personService;

    @GetMapping (path = "")
    public PageResponse<PersonEntity> getPersonList(
            @ModelAttribute PersonParamDto personParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return personService.getPersonList(personParamDto, pageRequest);
    }

    @GetMapping (path = "{id}")
    public PersonEntity getPerson(
            @PathVariable (name = "id") Long id) {
        return personService.getPerson(id);
    }

    @PostMapping (path = "", params = {"bulk"})
    public List<PersonEntity> createPersonList(
            @RequestBody List<PersonDto> personDtoList) {
        List<PersonEntity> personList = new ArrayList<>();
        for (int i = 0; i < personDtoList.size(); i++) {
            PersonDto personDto = personDtoList.get(i);
            PersonEntity personEntity = PersonEntity.builder()
                    .name(personDto.getName())
                    .build();
            personList.add(personEntity);
        }
        return personService.createPersonList(personList);
    }

    @PostMapping (path = "", params = {"!bulk"})
    public PersonEntity createPerson(
            @RequestBody PersonDto personDto) {
        PersonEntity personEntity = PersonEntity.builder()
                .name(personDto.getName())
                .build();
        return personService.createPerson(personEntity);
    }

    @PutMapping (path = "")
    public List<PersonEntity> modifyPersonList(
            @RequestBody List<PersonDto> personDtoList) {
        List<PersonEntity> personList = new ArrayList<>();
        for (int i = 0; i < personDtoList.size(); i++) {
            PersonDto personDto = personDtoList.get(i);
            PersonEntity personEntity = PersonEntity.builder()
                    .id(personDto.getId())
                    .name(personDto.getName())
                    .build();
            personList.add(personEntity);
        }
        return personService.modifyPersonList(personList);
    }

    @PutMapping (path = "{id}")
    public PersonEntity modifyPerson(
            @PathVariable (name = "id") Long id,
            @RequestBody PersonDto personDto) {
        PersonEntity personEntity = PersonEntity.builder()
                .id(id)
                .name(personDto.getName())
                .build();
        return personService.modifyPerson(personEntity);
    }

    @DeleteMapping (path = "")
    public void removePersonList(
            @RequestBody List<Long> idList) {
        personService.removePersonList(idList);
    }

    @DeleteMapping (path = "{id}")
    public void removePerson(
            @PathVariable (name = "id") Long id) {
        personService.removePerson(id);
    }

}