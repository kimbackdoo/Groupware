package kr.co.metasoft.groupware.api.common.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.metasoft.groupware.api.common.dto.RoleUserDto;
import kr.co.metasoft.groupware.api.common.dto.RoleUserParamDto;
import kr.co.metasoft.groupware.api.common.dto.VacationParamDto;
import kr.co.metasoft.groupware.api.common.entity.ApprovalEntity;
import kr.co.metasoft.groupware.api.common.entity.RoleUserEntity;
import kr.co.metasoft.groupware.api.common.entity.UserEntity;
import kr.co.metasoft.groupware.api.common.entity.VacationEntity;
import kr.co.metasoft.groupware.api.common.mapper.RoleUserMapper;
import kr.co.metasoft.groupware.common.util.PageRequest;
import kr.co.metasoft.groupware.common.util.PageResponse;

@Valid
@Service
public class VacationDataDownloadService {

    @Autowired
    private VacationService vacationService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSealService userSealService;

    @Autowired
    private RoleUserMapper roleUserMapper;

    @Value (value = "${groupware.file.load.url.vacation.form}")
    private String filePath;


    @Transactional(readOnly = true)
    public byte[] getVacationXlsx(VacationParamDto vacationParamDto) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        VacationEntity vacationEntity = vacationService.getVacation(vacationParamDto.getId());
        ApprovalEntity approvalEntity = approvalService.getApprovalEntity(vacationEntity.getId());
        UserEntity userEntity = userService.getUser(vacationEntity.getUserId());


        //불러올 파일 경로 수정필요
        FileInputStream file = new FileInputStream(filePath);

        try(XSSFWorkbook workbook = new XSSFWorkbook(file)){

            XSSFSheet sheet = workbook.getSheetAt(0);


            //결제 도장
            //팀장(userId = 16), 이사(userId = 17), 대표이사(userId = 1) => 결재 도장 url 불러오기
            String teamLeaderSeal = userSealService.selectUserseal(16L).getImageUrl();
            String directorSeal = userSealService.selectUserseal(17L).getImageUrl();
            String presidentSeal = userSealService.selectUserseal(1L).getImageUrl();

            //먼저 role 값이 사원, 팀장, 이사, 대표이사 인지 구분
            RoleUserParamDto r = RoleUserParamDto.builder().userId(userEntity.getId()).build();
            PageRequest p = new PageRequest();
            List<RoleUserEntity> list = roleUserMapper.selectRoleUserList(r,p);

            //휴가신청자 싸인
            String signPath = userSealService.selectUserseal(userEntity.getId()).getSignUrl();
            addExcelImage(workbook, sheet, signPath, 27, 6, 0.3);

            //휴가신청자 직급
            String roleValue = list.get(0).getRole().getValue();

            //해당 approval에 각각
            switch (roleValue) {
            case "ROLE_EMPLOYEE":
                roleValue = "사원";
                addExcelImage(workbook, sheet, teamLeaderSeal, 4, 7, 0.15);
                addExcelImage(workbook, sheet, directorSeal, 4, 8, 0.15);
                addExcelImage(workbook, sheet, presidentSeal, 4, 9, 0.15);
                break;
            case "ROLE_TEAMLEADER":
                roleValue = "팀장";
                addExcelImage(workbook, sheet, directorSeal, 4, 8, 0.15);
                addExcelImage(workbook, sheet, presidentSeal, 4, 9, 0.15);
                break;
            case "ROLE_DIRECTOR":
                roleValue = "이사";
                addExcelImage(workbook, sheet, presidentSeal, 4, 9, 0.15);
                break;
            case "ROLE_ADMIN":
                roleValue = "대표 이사";
                addExcelImage(workbook, sheet, presidentSeal, 4, 9, 0.15);
                break;
            default:
                break;
            }

            XSSFRow row = null;
            XSSFCell cell = null;
            XSSFCellStyle headerCellStyle = null;
            XSSFCellStyle bodyCellStyle = null;
            XSSFFont headerFont = null;
            XSSFFont bodyFont = null;
            headerFont = workbook.createFont();
            headerFont.setFontName("맑은 고딕");
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            bodyFont = workbook.createFont();
            bodyFont.setFontName("맑은 고딕");
            bodyFont.setFontHeightInPoints((short) 11);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setFont(headerFont);
            bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
            bodyCellStyle.setBorderTop(BorderStyle.THIN);
            bodyCellStyle.setBorderRight(BorderStyle.THIN);
            bodyCellStyle.setBorderBottom(BorderStyle.THIN);
            bodyCellStyle.setBorderLeft(BorderStyle.THIN);
            bodyCellStyle.setFont(bodyFont);


            //부서
            row = sheet.getRow(8); cell = row.getCell(1); cell.setCellValue(vacationEntity.getDepartment());

            //직급
            row = sheet.getRow(8); cell = row.getCell(6); cell.setCellValue(roleValue);

            //성 명
            row = sheet.getRow(9); cell = row.getCell(1); cell.setCellValue(userEntity.getUsername());

            //비상연락망
            row = sheet.getRow(9); cell = row.getCell(6); cell.setCellValue(vacationEntity.getEmergencyNum());


            //구분 (값에 따라 CASE 구분하기 )
            switch (vacationEntity.getType()) {
            case "M":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("▣ 월 차           □ 연 차           □ 반 차           □ 병 가           □ 기 타");
                break;
            case "N":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("□ 월 차           ▣ 연 차           □ 반 차           □ 병 가           □ 기 타");
                break;
            case "O":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("□ 월 차           □ 연 차           ▣ 반 차           □ 병 가           □ 기 타");
                break;
            case "S":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("□ 월 차           □ 연 차           □ 반 차           ▣ 병 가           □ 기 타");
                break;
            default:
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("□ 월 차           □ 연 차           □ 반 차           □ 병 가           ▣ 기 타");
                break;
            }


            //휴가 기간
            String sterm = String.valueOf(vacationEntity.getSterm());
            String sYear = sterm.substring(0, 4);
            String sMonth = sterm.substring(5, 7);
            String sDay = sterm.substring(8, 10);
            String eterm = String.valueOf(vacationEntity.getEterm());
            String eYear = eterm.substring(0, 4);
            String eMonth = eterm.substring(5, 7);
            String eDay = eterm.substring(8, 10);
            row = sheet.getRow(11); cell = row.getCell(1); cell.setCellValue(sYear+"년    "+sMonth+"월    "+sDay+"일    ~    "+eYear+"년    "+eMonth+"월    "+eDay+"일");


            //세부 사항
            row = sheet.getRow(12); cell = row.getCell(1); cell.setCellValue(vacationEntity.getDetail());

            //인수 인계자
            String takingUser = vacationEntity.getTakingUser();
            if(takingUser == null) takingUser = "";
            row = sheet.getRow(18); cell = row.getCell(1); cell.setCellValue(
                    "                             인수인계자 :    " + takingUser);

            //휴가 신청 날짜 입력 + 신청자 인
            Calendar cal = Calendar.getInstance();
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.valueOf(cal.get(Calendar.MONTH)+1);
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            row = sheet.getRow(19); cell = row.getCell(0); cell.setCellValue(
                    " 위와 같이 휴가를 신청합니다.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    year+
                    "년             "+
                    month+
                    "월              "+
                    day+
                    "일\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "신청자 :     "+
                    userEntity.getUsername()+
                    "      (인)");


//            String path = "C:\\";
//            String fileName = "휴가신청서_"+userEntity.getUsername()+"("+vacationEntity.getId()+")"+".xlsx";
//            File xlsxFile = new File(path + fileName);
//            FileOutputStream fileOut = new FileOutputStream(xlsxFile);
//            workbook.write(fileOut);

            workbook.write(baos);

        }catch (Exception e) {

            System.out.println("=========================================");
            System.out.println(e);
            System.out.println("=========================================");
        }

        //baos 출력
        return baos.toByteArray();
    }

    public void addExcelImage(XSSFWorkbook workbook, XSSFSheet sheet,String fileUrl, int row, int col,double size) throws IOException {
        String picturePath = fileUrl;

        InputStream is = new FileInputStream(picturePath);
        byte[] bytes = IOUtils.toByteArray(is);
        int picIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
        is.close();

        XSSFCreationHelper helper = workbook.getCreationHelper();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = helper.createClientAnchor();

        //이미지 cell 위치
        anchor.setRow1(row);
        anchor.setDx1(XSSFShape.EMU_PER_PIXEL*6);
        anchor.setCol1(col);
        anchor.setDy1(XSSFShape.EMU_PER_PIXEL*4);

        XSSFPicture pict = drawing.createPicture(anchor, picIdx);
        pict.resize();
        pict.resize(size, size);
    }



}
