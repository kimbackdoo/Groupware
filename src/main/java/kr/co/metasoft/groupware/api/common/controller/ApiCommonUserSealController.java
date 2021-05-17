package kr.co.metasoft.groupware.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.api.common.dto.UserSealDto;
import kr.co.metasoft.groupware.api.common.entity.UserSealEntity;
import kr.co.metasoft.groupware.api.common.service.UserSealService;

@RestController
@RequestMapping(path = "api/app/userSeals")
public class ApiCommonUserSealController {

    @Autowired
    private UserSealService userSealService;

    @GetMapping(path = "{userId}")
    public UserSealEntity getUserSeal(
            @PathVariable (name = "userId") Long userId) {
        return userSealService.selectUserseal(userId);
    }

    @PostMapping(path = "")
    public UserSealEntity createUserSeal(
            @RequestBody UserSealDto userSealDto) {
        UserSealEntity userSealEntity = UserSealEntity.builder()
                .userId(userSealDto.getUserId())
                .imageUrl(userSealDto.getImageUrl())
                .signUrl(userSealDto.getSignUrl())
                .build();
        return userSealService.createUserSeal(userSealEntity);
    }

    @PutMapping(path = "{id}")
    public UserSealEntity modifyUserSeal(
            @PathVariable (name = "id") Long id,
            @RequestBody UserSealDto userSealDto) {

        UserSealEntity userSealEntity = UserSealEntity.builder()
                .id(id)
                .userId(userSealDto.getUserId())
                .imageUrl(userSealDto.getImageUrl())
                .signUrl(userSealDto.getSignUrl())
                .build();
        return userSealService.modifyUserSeal(userSealEntity);
    }

    @DeleteMapping(path = "{id}")
    public void deleteUserSeal(
            @PathVariable (name = "id") Long id) {
        userSealService.removeUserSeal(id);
    }



}
