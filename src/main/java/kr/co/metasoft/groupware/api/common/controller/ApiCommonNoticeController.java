package kr.co.metasoft.groupware.api.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kr.co.metasoft.groupware.api.common.dto.Notice2Dto;
import kr.co.metasoft.groupware.api.common.dto.Notice2ParamDto;
import kr.co.metasoft.groupware.api.common.entity.NoticeEntity;
import kr.co.metasoft.groupware.api.common.service.NoticeService;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/app/notices")
public class ApiCommonNoticeController {


    @Autowired
    private NoticeService noticeService;

    @GetMapping(path = "")
    public PageResponse<NoticeEntity> getNoticeList(
            @ModelAttribute Notice2ParamDto notice2ParamDto,
            @ModelAttribute PageRequest pageRequest){
        return noticeService.selectNoticeList(notice2ParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public NoticeEntity getNotice(
            @PathVariable (name = "id") Long id) {
        return noticeService.selectNotice(id);
    }

    @PostMapping(path = "")
    public NoticeEntity createNotice(
            @RequestBody Notice2Dto notice2Dto) {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .userId(notice2Dto.getUserId())
                .title(notice2Dto.getTitle())
                .contents(notice2Dto.getContents())
                .postingDate(notice2Dto.getPostingDate())
                .build();
        return noticeService.createNotice(noticeEntity);
    }

    @PutMapping(path = "{id}")
    public NoticeEntity modifyNotice(
            @PathVariable (name = "id") Long id,
            @RequestBody Notice2Dto notice2Dto) {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .id(id)
                .userId(notice2Dto.getUserId())
                .title(notice2Dto.getTitle())
                .contents(notice2Dto.getContents())
                .postingDate(notice2Dto.getPostingDate())
                .build();
        return noticeService.modifyNotice(noticeEntity);
    }

    @DeleteMapping(path ="{id}")
    public void removeNotice(
            @PathVariable (name = "id") Long id) {
        noticeService.removeNotice(id);
    }

    @DeleteMapping(path = "")
    public void removeNoticeList(
            @RequestBody List<Long> idList) {
        noticeService.removeNoticeList(idList);
    }

}
