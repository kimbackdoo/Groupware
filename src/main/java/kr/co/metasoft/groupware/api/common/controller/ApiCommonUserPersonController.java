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

import kr.co.metasoft.groupware.api.common.dto.UserPersonDto;
import kr.co.metasoft.groupware.api.common.dto.UserPersonParamDto;
import kr.co.metasoft.groupware.api.common.entity.UserPersonEntity;
import kr.co.metasoft.groupware.api.common.service.UserPersonService;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;

@RestController
@RequestMapping (path = "api/common/user-people")
public class ApiCommonUserPersonController {

    @Autowired
    private UserPersonService userPersonService;

    @GetMapping (path = "")
    public PageResponse<UserPersonEntity> getUserPersonList(
            @ModelAttribute UserPersonParamDto userPersonParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return userPersonService.getUserPersonList(userPersonParamDto, pageRequest);
    }

    @GetMapping (path = "{userId}")
    public UserPersonEntity getUserPerson(
            @PathVariable (name = "userId") Long userId) {
        return userPersonService.getUserPerson(userId);
    }

    @PostMapping (path = "", params = {"bulk"})
    public List<UserPersonEntity> createUserPersonList(
            @RequestBody List<UserPersonDto> userPersonDtoList) {
        List<UserPersonEntity> userPersonList = new ArrayList<>();
        for (int i = 0; i < userPersonDtoList.size(); i++) {
            UserPersonDto userPersonDto = userPersonDtoList.get(i);
            UserPersonEntity userPersonEntity = UserPersonEntity.builder()
                    .userId(userPersonDto.getUserId())
                    .personId(userPersonDto.getPersonId())
                    .build();
            userPersonList.add(userPersonEntity);
        }
        return userPersonService.createUserPersonList(userPersonList);
    }

    @PostMapping (path = "", params = {"!bulk"})
    public UserPersonEntity createUserPerson(
            @RequestBody UserPersonDto userPersonDto) {
        UserPersonEntity userPersonEntity = UserPersonEntity.builder()
                .userId(userPersonDto.getUserId())
                .personId(userPersonDto.getPersonId())
                .build();
        return userPersonService.createUserPerson(userPersonEntity);
    }

    @PutMapping (path = "")
    public List<UserPersonEntity> modifyUserPersonList(
            @RequestBody List<UserPersonDto> userPersonDtoList) {
        List<UserPersonEntity> userPersonList = new ArrayList<>();
        for (int i = 0; i < userPersonDtoList.size(); i++) {
            UserPersonDto userPersonDto = userPersonDtoList.get(i);
            UserPersonEntity userPersonEntity = UserPersonEntity.builder()
                    .userId(userPersonDto.getUserId())
                    .personId(userPersonDto.getPersonId())
                    .build();
            userPersonList.add(userPersonEntity);
        }
        return userPersonService.modifyUserPersonList(userPersonList);
    }

    @PutMapping (path = "{userId}")
    public UserPersonEntity modifyUserPerson(
            @PathVariable (name = "userId") Long userId,
            @RequestBody UserPersonDto userPersonDto) {
        UserPersonEntity userPersonEntity = UserPersonEntity.builder()
                .userId(userId)
                .personId(userPersonDto.getPersonId())
                .build();
        return userPersonService.modifyUserPerson(userPersonEntity);
    }

    @DeleteMapping (path = "")
    public void removeUserPersonList(
            @RequestBody List<Long> userIdList) {
        userPersonService.removeUserPersonList(userIdList);
    }

    @DeleteMapping (path = "{userId}")
    public void removeUserPerson(
            @PathVariable (name = "userId") Long userId) {
        userPersonService.removeUserPerson(userId);
    }

}