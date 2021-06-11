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
import org.springframework.web.multipart.MultipartFile;

import kr.co.metasoft.groupware.api.app.dto.SealImageDto;
import kr.co.metasoft.groupware.api.app.service.SealSaveService;
import kr.co.metasoft.groupware.api.common.dto.UserSealDto;
import kr.co.metasoft.groupware.api.common.entity.UserSealEntity;
import kr.co.metasoft.groupware.api.common.service.UserSealService;

@RestController
@RequestMapping(path = "api/app/userSeals")
public class ApiCommonUserSealController {

    @Autowired
    private SealSaveService sealSaveService;

    @Autowired
    private UserSealService userSealService;

    @GetMapping(path = "{userId}")
    public UserSealEntity getUserSeal(
            @PathVariable (name = "userId") Long userId) {
        return userSealService.selectUserseal(userId);
    }

    @PostMapping(path = "")
    public UserSealEntity createUserSeal(
            @RequestBody SealImageDto sealImageDto, Long userId) {
        UserSealDto userSealDto = sealSaveService.createSealDto(sealImageDto, userId);
        UserSealEntity userSealEntity = UserSealEntity.builder()
                .userId(userSealDto.getUserId())
                .sealImage(userSealDto.getSealImage())
                .signImage(userSealDto.getSignImage())
                .build();
        return userSealService.createUserSeal(userSealEntity);
    }

    @PutMapping(path = "modify")
    public UserSealEntity modifyUserSeal(
            @RequestBody UserSealDto userSealDto) {

        //multipartFile 변환하기
        MultipartFile signMultiFile = userSealDto.getSignMultiFile();
        MultipartFile sealMultiFile = userSealDto.getSealMultiFile();
        SealImageDto sealImageDto = SealImageDto.builder()
                                    .signImage(signMultiFile)
                                    .sealImage(sealMultiFile)
                                    .build();
        UserSealDto userSealDto2 = sealSaveService.createSealDto(sealImageDto, userSealDto.getUserId());

        UserSealEntity userSealEntity = UserSealEntity.builder()
                .id(userSealDto.getId())
                .userId(userSealDto2.getUserId())
                .sealName(userSealDto2.getSealName())
                .signName(userSealDto2.getSignName())
                .sealImage(userSealDto2.getSealImage())
                .signImage(userSealDto2.getSignImage())
                .build();
        return userSealService.modifyUserSeal(userSealEntity);
    }

    @DeleteMapping(path = "{id}")
    public void deleteUserSeal(
            @PathVariable (name = "id") Long id) {
        userSealService.removeUserSeal(id);
    }



}
