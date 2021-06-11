package kr.co.metasoft.groupware.api.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import kr.co.metasoft.groupware.api.app.dto.AccountDto;
import kr.co.metasoft.groupware.api.app.dto.SealImageDto;
import kr.co.metasoft.groupware.api.common.dto.RoleUserParamDto;
import kr.co.metasoft.groupware.api.common.dto.UserSealDto;
import kr.co.metasoft.groupware.api.common.entity.PersonEntity;
import kr.co.metasoft.groupware.api.common.entity.RoleUserEntity;
import kr.co.metasoft.groupware.api.common.entity.UserEntity;
import kr.co.metasoft.groupware.api.common.entity.UserPersonEntity;
import kr.co.metasoft.groupware.api.common.entity.UserSealEntity;
import kr.co.metasoft.groupware.api.common.repository.PersonRepository;
import kr.co.metasoft.groupware.api.common.repository.RoleUserRepository;
import kr.co.metasoft.groupware.api.common.repository.UserPersonRepository;
import kr.co.metasoft.groupware.api.common.repository.UserRepository;
import kr.co.metasoft.groupware.api.common.service.RoleUserService;
import kr.co.metasoft.groupware.api.common.service.UserSealService;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;


@Service
public class AccountService {

    @Autowired
    RoleUserService roleUserService;

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SealSaveService sealSaveService;

    @Autowired
    private UserSealService userSealService;

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public AccountDto createAccount(@Valid @NotNull(groups = { CreateValidationGroup.class }) AccountDto accountDto) {
        AccountDto result = new AccountDto();

        UserEntity userEntity = new UserEntity();

        if(accountDto.getUserDto().getUsername() != null && !accountDto.getUserDto().getUsername().equals(""))
            userEntity.setUsername(accountDto.getUserDto().getUsername());

        if(accountDto.getUserDto().getPassword() != null)
            userEntity.setPassword(accountDto.getUserDto().getPassword());

        if(accountDto.getUserDto().getEmail() != null)
            userEntity.setEmail(accountDto.getUserDto().getEmail());

        if(accountDto.getUserDto().getStatus() != null)
            userEntity.setStatus(accountDto.getUserDto().getStatus());

        PersonEntity personEntity = new PersonEntity(accountDto.getPersonDto().getId(),
                accountDto.getPersonDto().getName(), accountDto.getPersonDto().getCreatedDate(),
                accountDto.getPersonDto().getLastModifiedDate());

        userEntity.setPassword(passwordEncoder.encode((userEntity.getPassword())));
        Long userId = userRepository.save(userEntity).getId();
        Long personId = personRepository.save(personEntity).getId();
        result.setUserId(userId);
        result.setPersonId(personId);
        UserPersonEntity userPersonEntity = new UserPersonEntity(userId, personId, null, null);

        List<RoleUserEntity> roleUserEntityList = new ArrayList<RoleUserEntity>();
        for (int i = 0; i < accountDto.getRoleId().size(); i++) {
            roleUserEntityList.add(new RoleUserEntity(userId, accountDto.getRoleId().get(i), null, null));
        }
        roleUserRepository.saveAll(roleUserEntityList);
        userPersonRepository.save(userPersonEntity);
        return result;
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyAccount(@Valid @NotNull(groups = { ModifyValidationGroup.class }) AccountDto accountDto) {


        UserEntity userEntity = (userRepository.findById(accountDto.getUserDto().getId())).orElse(new UserEntity());


        if(accountDto.getUserDto().getUsername() != null && !accountDto.getUserDto().getUsername().equals(""))
            userEntity.setUsername(accountDto.getUserDto().getUsername());

        if(accountDto.getUserDto().getPassword() != null)
            userEntity.setPassword(accountDto.getUserDto().getPassword());

        if(accountDto.getUserDto().getEmail() != null)
            userEntity.setEmail(accountDto.getUserDto().getEmail());

        if(accountDto.getUserDto().getStatus() != null)
            userEntity.setStatus(accountDto.getUserDto().getStatus());


        PersonEntity personEntity = new PersonEntity(accountDto.getPersonDto().getId(),
                accountDto.getPersonDto().getName(), accountDto.getPersonDto().getCreatedDate(),
                accountDto.getPersonDto().getLastModifiedDate());
        Long userId = userRepository.save(userEntity).getId();
        Long personId = personRepository.save(personEntity).getId();
        UserPersonEntity userPersonEntity = new UserPersonEntity(userId, personId, null, null);
        List<RoleUserEntity> roleUserEntityList = new ArrayList<>();
        for (int i = 0; i < accountDto.getRoleId().size(); i++) {
            roleUserEntityList.add(new RoleUserEntity(userId, accountDto.getRoleId().get(i), null, null));
        }
        roleUserRepository.saveAll(roleUserEntityList);
        userPersonRepository.save(userPersonEntity);

        RoleUserParamDto roleUserParamDto = new RoleUserParamDto();
        roleUserParamDto.setUserId(accountDto.getUserId());
        roleUserParamDto.setRoleId(null);

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setRowSize(1000000);

        PageResponse<RoleUserEntity> pageResponse = roleUserService.getRoleUserList(roleUserParamDto, pageRequest);
        List<RoleUserEntity> resultList = pageResponse.getItems();

        int i, j;
        for (i=0; i<resultList.size(); i++) {
            if (!(resultList.get(i).getUserId().equals(accountDto.getUserId()))) {
                break;
            }
            else {
                for (j=0; j<accountDto.getRoleId().size(); j++) {
                    if (resultList.get(i).getRoleId() == accountDto.getRoleId().get(j))
                        break;
                }
                if (j==accountDto.getRoleId().size())
                    roleUserService.removeRoleUser(resultList.get(i).getRoleId(), resultList.get(i).getUserId());
            }
        }
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public void userSignUp(@Valid @NotNull(groups = { CreateValidationGroup.class }) AccountDto accountDto) {

        String password = accountDto.getPassword();
        password = passwordEncoder.encode((password));
        UserEntity userEntity = UserEntity.builder()
                            .birthDate(accountDto.getBirthDate())
                            .department(accountDto.getDepartment())
                            .teamName(accountDto.getTeamName())
                            .position(accountDto.getPosition())
                            .name(accountDto.getName())
                            .username(accountDto.getUsername())
                            .password(password)
                            .email(accountDto.getEmail())
                            .status("F")
                            .lastModifiedPasswordDate(LocalDate.now())
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();


        PersonEntity personEntity = PersonEntity.builder()
                            .name(accountDto.getName())
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();



        Long userId = userRepository.save(userEntity).getId();
        Long personId = personRepository.save(personEntity).getId();
        System.out.println("userId : "+userId );
        UserPersonEntity userPersonEntity = new UserPersonEntity();
        userPersonEntity.setUserId(userId);
        userPersonEntity.setPersonId(personId);

        RoleUserEntity roleUserEntity = new RoleUserEntity();

        roleUserEntity.setUserId(userId);
        roleUserEntity.setRoleId(Long.valueOf(2));

        roleUserRepository.save(roleUserEntity);
        userPersonRepository.save(userPersonEntity);


        System.out.println("----------------Seal 저장-------------------------------");
        //SealService 만들기
        MultipartFile signImage = accountDto.getSignImage();
        System.out.println("통과1");
        System.out.println("accountDto.getSealImageDto().getSignImage() 값 : "+ accountDto.getSignImage());
        System.out.println("signImage : "+ signImage);
        System.out.println("signImage 파일이름: "+ signImage.getOriginalFilename());
        System.out.println("signImage 사이즈 : "+ signImage.getSize());
        MultipartFile sealImage = accountDto.getSealImage();
        System.out.println("통과2");
        System.out.println("accountDto.getSealImageDto().getSealImage() 값 : "+ accountDto.getSealImage());
        System.out.println("sealImage : "+ sealImage);
        System.out.println("sealImage 파일이름 : "+ sealImage.getOriginalFilename());
        System.out.println("sealImage 사이즈 : "+ sealImage.getSize());
        SealImageDto sealImageDto = SealImageDto.builder()
                                    .signImage(signImage)
                                    .sealImage(sealImage)
                                    .build();
        UserSealDto userSealDto = sealSaveService.createSealDto(sealImageDto, userId);
        System.out.println("----------------userSealDto 생성-------------------------------");
        UserSealEntity userSealEntity = UserSealEntity.builder()
                .userId(userId)
                .sealName(accountDto.getSealName())
                .signName(accountDto.getSignName())
                .sealImage(userSealDto.getSealImage())
                .signImage(userSealDto.getSignImage())
                .build();
        userSealService.createUserSeal(userSealEntity);
        System.out.println("----------------userSealEntity 저장 -------------------------------");


    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeAccount(@Valid @NotNull(groups = { RemoveValidationGroup.class }) AccountDto accountDto) {
        userPersonRepository.delete(UserPersonEntity.builder().userId(accountDto.getUserId()).build());
        userRepository.delete(UserEntity.builder().id(accountDto.getUserId()).build());
        personRepository.delete(PersonEntity.builder().id(accountDto.getPersonId()).build());
        roleUserRepository.delete(RoleUserEntity.builder().userId(accountDto.getUserId()).build());
    }

}
