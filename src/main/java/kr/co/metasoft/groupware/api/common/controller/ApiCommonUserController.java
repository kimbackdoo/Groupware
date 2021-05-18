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

import kr.co.metasoft.groupware.api.common.dto.UserDto;
import kr.co.metasoft.groupware.api.common.dto.UserParamDto;
import kr.co.metasoft.groupware.api.common.entity.UserEntity;
import kr.co.metasoft.groupware.api.common.service.UserService;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;

@RestController
@RequestMapping (path = "api/common/users")
public class ApiCommonUserController {

    @Autowired
    private UserService userService;

    @GetMapping (path = "")
    public PageResponse<UserEntity> getUserList(
            @ModelAttribute UserParamDto userParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return userService.getUserList(userParamDto, pageRequest);
    }

    @GetMapping (path = "{id}")
    public UserEntity getUser(
            @PathVariable (name = "id") Long id) {
        return userService.getUser(id);
    }

    @PostMapping (path = "", params = {"bulk"})
    public List<UserEntity> createUserList(
            @RequestBody List<UserDto> userDtoList) {
        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < userDtoList.size(); i++) {
            UserDto userDto = userDtoList.get(i);
            UserEntity userEntity = UserEntity.builder()
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .email(userDto.getEmail())
                    .birthDate(userDto.getBirthDate())
                    .department(userDto.getDepartment())
                    .teamName(userDto.getTeamName())
                    .position(userDto.getPosition())
                    .lastModifiedPasswordDate(userDto.getLastModifiedPasswordDate())
                    .status(userDto.getStatus())
                    .build();
            userList.add(userEntity);
        }
        return userService.createUserList(userList);
    }

    @PostMapping (path = "", params = {"!bulk"})
    public UserEntity createUser(
            @RequestBody UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .birthDate(userDto.getBirthDate())
                .department(userDto.getDepartment())
                .teamName(userDto.getTeamName())
                .position(userDto.getPosition())
                .lastModifiedPasswordDate(userDto.getLastModifiedPasswordDate())
                .status(userDto.getStatus())
                .build();
        return userService.createUser(userEntity);
    }

    @PutMapping (path = "")
    public List<UserEntity> modifyUserList(
            @RequestBody List<UserDto> userDtoList) {
        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < userDtoList.size(); i++) {
            UserDto userDto = userDtoList.get(i);
            UserEntity userEntity = UserEntity.builder()
                    .id(userDto.getId())
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .email(userDto.getEmail())
                    .birthDate(userDto.getBirthDate())
                    .department(userDto.getDepartment())
                    .teamName(userDto.getTeamName())
                    .position(userDto.getPosition())
                    .lastModifiedPasswordDate(userDto.getLastModifiedPasswordDate())
                    .status(userDto.getStatus())
                    .build();
            userList.add(userEntity);
        }
        return userService.modifyUserList(userList);
    }

    @PutMapping (path = "{id}")
    public UserEntity modifyUser(
            @PathVariable (name = "id") Long id,
            @RequestBody UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .id(id)
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .birthDate(userDto.getBirthDate())
                .department(userDto.getDepartment())
                .teamName(userDto.getTeamName())
                .position(userDto.getPosition())
                .lastModifiedPasswordDate(userDto.getLastModifiedPasswordDate())
                .status(userDto.getStatus())
                .build();
        return userService.modifyUser(userEntity);
    }

    @DeleteMapping (path = "")
    public void removeUserList(
            @RequestBody List<Long> idList) {
        userService.removeUserList(idList);
    }

    @DeleteMapping (path = "{id}")
    public void removeUser(
            @PathVariable (name = "id") Long id) {
        userService.removeUser(id);
    }

    /*
     * @PutMapping (path = "{id}/password") public void modifyPassword(
     *
     * @PathVariable (name = "id") Long id,
     *
     * @RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse
     * response) throws ServletException { userService.modifyPassword(id,
     * userDto.getOldPassword(), userDto.getNewPassword()); }
     */

    @PostMapping (path = "/id-exists")
    public Integer userIDExsts(@RequestBody UserParamDto userParamDto) {
        return userService.getUserExists(userParamDto);
    }
}