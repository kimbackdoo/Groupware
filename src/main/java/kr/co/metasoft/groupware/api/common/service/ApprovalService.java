package kr.co.metasoft.groupware.api.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.co.metasoft.groupware.api.common.dto.ApprovalDto;
import kr.co.metasoft.groupware.api.common.dto.ApprovalParamDto;
import kr.co.metasoft.groupware.api.common.entity.ApprovalEntity;
import kr.co.metasoft.groupware.api.common.mapper.ApprovalMapper;
import kr.co.metasoft.groupware.api.common.repository.ApprovalRepository;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Repository
@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private ApprovalMapper approvalMapper;

    @Validated(value = {ReadValidationGroup.class})
    @Transactional
    public PageResponse<ApprovalEntity> getApprovalList (
            @Valid ApprovalParamDto approvalParamDto,
            PageRequest pageRequest){

        int totalRows = approvalMapper.selectApprovalListCount(approvalParamDto);
        List<ApprovalEntity> approvalList = approvalMapper.selectApprovalList(approvalParamDto, pageRequest);
        PageResponse<ApprovalEntity> pageResponse = new PageResponse<>(pageRequest, totalRows);
        pageResponse.setItems(approvalList);

        return pageResponse;
    }


    @Validated(value = {ReadValidationGroup.class})
    @Transactional
    public ApprovalEntity getApprovalEntity (
            @Valid @NotNull (groups = {ReadValidationGroup.class}) Long id) {
        return approvalMapper.selectApproval(id);
    }


    @Validated(value = {CreateValidationGroup.class})
    @Transactional
    public ApprovalEntity createApprovalEntity(
            @Valid @NotNull (groups = {CreateValidationGroup.class}) ApprovalEntity approvalEntity) {
         return approvalRepository.save(approvalEntity);
    }


    @Validated(value = {ModifyValidationGroup.class})
    @Transactional
    public ApprovalEntity modifyApprovalEntity(
            @Valid @NotNull (groups = {ModifyValidationGroup.class}) ApprovalEntity approvalEntity) {
        System.out.println("modiftApprova Service : "+ approvalEntity.getId());
        System.out.println("modiftApprova Service : "+ approvalEntity.getVacationId());
        System.out.println("modiftApprova Service : "+ approvalEntity.getTeamLeader());

        return approvalRepository.save(approvalEntity);
    }

    @Validated(value = {RemoveValidationGroup.class})
    @Transactional
    public void removeApproval(
            @Valid @NotNull (groups = {RemoveValidationGroup.class}) Long id) {
        approvalRepository.deleteById(id);
    }

    @Validated(value = {RemoveValidationGroup.class})
    @Transactional
    public void removeApprovalList(
            @Valid @NotEmpty (groups = {RemoveValidationGroup.class}) List<@NotNull (groups = {RemoveValidationGroup.class}) Long> idList) {
        List<ApprovalEntity> approvalEntityList = new ArrayList<>();
        for(int i=0;i<idList.size();i++) {
            Long id = idList.get(i);
            approvalEntityList.add(ApprovalEntity.builder().id(id).build());
        }
        approvalRepository.deleteAll(approvalEntityList);
    }

}
