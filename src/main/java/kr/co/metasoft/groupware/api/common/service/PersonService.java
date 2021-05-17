package kr.co.metasoft.groupware.api.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.co.metasoft.groupware.api.common.dto.PersonParamDto;
import kr.co.metasoft.groupware.api.common.entity.PersonEntity;
import kr.co.metasoft.groupware.api.common.mapper.PersonMapper;
import kr.co.metasoft.groupware.api.common.repository.PersonRepository;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PageResponse<PersonEntity> getPersonList(
            @Valid PersonParamDto personParamDto,
            PageRequest pageRequest) {
        Integer personListCount = personMapper.selectPersonListCount(personParamDto);
        List<PersonEntity> personList = personMapper.selectPersonList(personParamDto, pageRequest);
        PageResponse<PersonEntity> pageResponse = new PageResponse<>(pageRequest, personListCount);
        pageResponse.setItems(personList);
        return pageResponse;
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PersonEntity getPerson(
            @Valid @NotNull (groups = {ReadValidationGroup.class}) Long id) {
        return personMapper.selectPerson(id);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public List<PersonEntity> createPersonList(
            @Valid @NotEmpty (groups = {CreateValidationGroup.class}) List<@NotNull (groups = {CreateValidationGroup.class}) PersonEntity> personList) {
        return personRepository.saveAll(personList);
    }

    @Validated (value = {CreateValidationGroup.class})
    @Transactional
    public PersonEntity createPerson(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) PersonEntity personEntity) {
        return personRepository.save(personEntity);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public List<PersonEntity> modifyPersonList(
            @Valid @NotEmpty (groups = {ModifyValidationGroup.class}) List<@NotNull (groups = {ModifyValidationGroup.class}) PersonEntity> personList) {
        return personRepository.saveAll(personList);
    }

    @Validated (value = {ModifyValidationGroup.class})
    @Transactional
    public PersonEntity modifyPerson(
            @Valid @NotNull (groups = {ModifyValidationGroup.class}) PersonEntity personEntity) {
        return personRepository.save(personEntity);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removePersonList(
            @Valid @NotEmpty (groups = {RemoveValidationGroup.class}) List<@NotNull (groups = {RemoveValidationGroup.class}) Long> idList) {
        List<PersonEntity> personList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            personList.add(PersonEntity.builder().id(id).build());
        }
        personRepository.deleteAll(personList);
    }

    @Validated (value = {RemoveValidationGroup.class})
    @Transactional
    public void removePerson(
            @Valid @NotNull (groups = {RemoveValidationGroup.class}) Long id) {
        personRepository.delete(PersonEntity.builder().id(id).build());
    }

}