package kr.co.metasoft.groupware.api.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.co.metasoft.groupware.api.common.dto.Notice2ParamDto;
import kr.co.metasoft.groupware.api.common.entity.NoticeEntity;
import kr.co.metasoft.groupware.api.common.mapper.NoticeMapper;
import kr.co.metasoft.groupware.api.common.repository.NoticeRepository;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;
import kr.co.metasoft.groupware.common.validation.group.CreateValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ModifyValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.ReadValidationGroup;
import kr.co.metasoft.groupware.common.validation.group.RemoveValidationGroup;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeRepository noticeRepository;


    @Validated(value = {ReadValidationGroup.class})
    @Transactional
    public PageResponse<NoticeEntity> selectNoticeList(
            @Valid Notice2ParamDto notice2ParamDto,
            PageRequest pageRequest){

        int totalRows = noticeMapper.selectNoticeListCount(notice2ParamDto);
        List<NoticeEntity> noticeList = noticeMapper.selectNoticeList(notice2ParamDto, pageRequest);
        PageResponse<NoticeEntity> pageResponse = new PageResponse<>(pageRequest, totalRows);
        pageResponse.setItems(noticeList);
        return pageResponse;
    }


    @Validated(value = {ReadValidationGroup.class})
    @Transactional
    public NoticeEntity selectNotice(
            @Valid Long id) {
        return noticeMapper.selectNotice(id);
    }

    @Validated(value = {CreateValidationGroup.class})
    @Transactional
    public NoticeEntity createNotice(
            @Valid NoticeEntity noticeEntity) {
        return noticeRepository.save(noticeEntity);
    }


    @Validated(value = {ModifyValidationGroup.class})
    @Transactional
    public NoticeEntity modifyNotice(
            @Valid NoticeEntity noticeEntity) {
        return noticeRepository.save(noticeEntity);
    }

    @Validated(value = {RemoveValidationGroup.class})
    @Transactional
    public void removeNotice(
        @Valid Long id	) {
        noticeRepository.deleteById(id);
    }

    @Validated(value = {RemoveValidationGroup.class})
    @Transactional
    public void removeNoticeList(
        @Valid List<Long> idList) {
        List<NoticeEntity> noticeList = new ArrayList<>();
        for(int i=0;i<idList.size();i++) {
            Long id = idList.get(i);
            noticeList.add(NoticeEntity.builder().id(id).build());
        }
        noticeRepository.deleteAll(noticeList);
    }
}
