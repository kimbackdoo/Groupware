package kr.co.metasoft.groupware.api.common.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
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
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.OracleDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.metasoft.groupware.api.app.dto.ByteSealImageDto;
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
    private UserService userService;

    @Autowired
    private UserSealService userSealService;


    @Value (value = "${groupware.file.load.url.vacation.form}")
    private String filePath;


    @Transactional(readOnly = true)
    public byte[] getVacationXlsx(VacationParamDto vacationParamDto) throws IOException {
        byte[] dd = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        VacationEntity vacationEntity = vacationService.getVacation(vacationParamDto.getId());
        UserEntity userEntity = userService.getUser(vacationEntity.getUserId());


        //????????? ?????? ?????? ????????????
        FileInputStream file = new FileInputStream(filePath);

        try(XSSFWorkbook workbook = new XSSFWorkbook(file)){

            XSSFSheet sheet = workbook.getSheetAt(0);


            //???????????? ??????, ??????(user id)
            ByteSealImageDto myImageDto = userSealService.selectImageDto(userEntity.getId());
            byte[] mySeal = myImageDto.getSealImage(); //??????
            byte[] mySign = myImageDto.getSignImage(); //??????
            if(myImageDto.getSealImage() == null) {
                mySeal = mySign;
            }else if(myImageDto.getSignImage() == null) {
                mySign = mySeal;
            }

            //?????????
//                ByteArrayInputStream bis = new ByteArrayInputStream(a);
//                BufferedImage a1 = ImageIO.read(bis);
//                ImageIO.write(a1, "png", new File("C:\\Users\\?????????\\Desktop\\ITO ??????\\test.png"));
//                System.out.println("image created");
//


            //?????? ??????
            //???????????? ??????(userId = ??), ??????(userId = 2), ????????????(userId = 1) => ?????? ?????? url ????????????
            ByteSealImageDto directorImageDto = userSealService.selectImageDto(2L);
            byte[] directorSign = directorImageDto.getSignImage(); //??????
            byte[] directorSeal = directorImageDto.getSealImage(); //??????
            if(directorImageDto.getSealImage() == null) {
                directorSeal = directorSign;
            }else if(directorImageDto.getSignImage() == null) {
                directorSign = directorSeal;
            }

            ByteSealImageDto presidentImageDto = userSealService.selectImageDto(1L);
            byte[] presidentSign = presidentImageDto.getSignImage(); //??????
            byte[] presidentSeal = presidentImageDto.getSealImage(); //??????
            if(presidentImageDto.getSealImage() == null) {
                presidentSeal = presidentSign;
            }else if(presidentImageDto.getSignImage() == null) {
                presidentSign = presidentSeal;
            }

            //??????????????? ??????
            addExcelImage(workbook, sheet, mySign, 27, 6, 0.3);


            //??????????????? ??????
            String roleValue = userEntity.getPosition();


            //- ?????? -
            //?????? approval??? ??????
            switch (roleValue) {
            case "enginner":
                roleValue = "?????????";
                addExcelImage(workbook, sheet, mySeal, 4, 7, 0.15);
                addExcelImage(workbook, sheet, directorSeal, 4, 8, 0.15);
                addExcelImage(workbook, sheet, presidentSeal, 4, 9, 0.15);
                break;
            case "teamLeader":
                roleValue = "??????";
                addExcelImage(workbook, sheet, mySeal, 4, 7, 0.15);
                addExcelImage(workbook, sheet, directorSeal, 4, 8, 0.15);
                addExcelImage(workbook, sheet, presidentSeal, 4, 9, 0.15);
                break;
            case "director":
                roleValue = "??????";
                addExcelImage(workbook, sheet, directorSeal, 4, 8, 0.15);
                addExcelImage(workbook, sheet, presidentSeal, 4, 9, 0.15);
                break;
            case "jeju":
                roleValue = "??????!";
                addExcelImage(workbook, sheet, mySeal, 4, 7, 0.15);
                addExcelImage(workbook, sheet, directorSeal, 4, 8, 0.15);
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
            headerFont.setFontName("?????? ??????");
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            bodyFont = workbook.createFont();
            bodyFont.setFontName("?????? ??????");
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


            //??????
            row = sheet.getRow(8); cell = row.getCell(1); cell.setCellValue(vacationEntity.getDepartment());

            //??????
            row = sheet.getRow(8); cell = row.getCell(6); cell.setCellValue(roleValue);

            //??? ???
            row = sheet.getRow(9); cell = row.getCell(1); cell.setCellValue(userEntity.getUsername());

            //???????????????
            row = sheet.getRow(9); cell = row.getCell(6); cell.setCellValue(vacationEntity.getEmergencyNum());


            //?????? (?????? ?????? CASE ???????????? )
            switch (vacationEntity.getType()) {
            case "M":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???");
                break;
            case "N":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???");
                break;
            case "O":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???");
                break;
            case "S":
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???");
                break;
            default:
                row = sheet.getRow(10); cell = row.getCell(1); cell.setCellValue("??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???           ??? ??? ???");
                break;
            }


            //?????? ??????
            String sterm = String.valueOf(vacationEntity.getSterm());
            String sYear = sterm.substring(0, 4);
            String sMonth = sterm.substring(5, 7);
            String sDay = sterm.substring(8, 10);
            String eterm = String.valueOf(vacationEntity.getEterm());
            String eYear = eterm.substring(0, 4);
            String eMonth = eterm.substring(5, 7);
            String eDay = eterm.substring(8, 10);
            row = sheet.getRow(11); cell = row.getCell(1); cell.setCellValue(sYear+"???    "+sMonth+"???    "+sDay+"???    ~    "+eYear+"???    "+eMonth+"???    "+eDay+"???");


            //?????? ??????
            row = sheet.getRow(12); cell = row.getCell(1); cell.setCellValue(vacationEntity.getDetail());

            //?????? ?????????
            String takingUser = vacationEntity.getTakingUser();
            if(takingUser == null) takingUser = "";
            row = sheet.getRow(18); cell = row.getCell(1); cell.setCellValue(
                    "                             ??????????????? :    " + takingUser);

            //?????? ?????? ?????? ?????? + ????????? ???
            Calendar cal = Calendar.getInstance();
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.valueOf(cal.get(Calendar.MONTH)+1);
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            row = sheet.getRow(19); cell = row.getCell(0); cell.setCellValue(
                    " ?????? ?????? ????????? ???????????????.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    year+
                    "???             "+
                    month+
                    "???              "+
                    day+
                    "???\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "????????? :     "+
                    userEntity.getUsername()+
                    "      (???)");


            workbook.write(baos);

        }catch (Exception e) {

            System.out.println("=========================================");
            System.out.println(e);
            System.out.println("=========================================");
        }

        //baos ??????
        return baos.toByteArray();
    }

    public void addExcelImage(XSSFWorkbook workbook, XSSFSheet sheet,byte[] content, int row, int col,double size) throws IOException {


        byte[] bytes = content;
        int picIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);

        XSSFCreationHelper helper = workbook.getCreationHelper();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = helper.createClientAnchor();

        //????????? cell ??????
        anchor.setRow1(row);
        anchor.setDx1(XSSFShape.EMU_PER_PIXEL*6);
        anchor.setCol1(col);
        anchor.setDy1(XSSFShape.EMU_PER_PIXEL*4);

        XSSFPicture pict = drawing.createPicture(anchor, picIdx);
        pict.resize();
        pict.resize(size, size);
    }

    //    public void addExcelImage(XSSFWorkbook workbook, XSSFSheet sheet,String fileUrl, int row, int col,double size) throws IOException {
//    	String picturePath = fileUrl;
//
//    	InputStream is = new FileInputStream(picturePath);
//    	byte[] bytes = IOUtils.toByteArray(is);
//    	int picIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
//    	is.close();
//
//    	XSSFCreationHelper helper = workbook.getCreationHelper();
//    	XSSFDrawing drawing = sheet.createDrawingPatriarch();
//    	XSSFClientAnchor anchor = helper.createClientAnchor();
//
//    	//????????? cell ??????
//    	anchor.setRow1(row);
//    	anchor.setDx1(XSSFShape.EMU_PER_PIXEL*6);
//    	anchor.setCol1(col);
//    	anchor.setDy1(XSSFShape.EMU_PER_PIXEL*4);
//
//    	XSSFPicture pict = drawing.createPicture(anchor, picIdx);
//    	pict.resize();
//    	pict.resize(size, size);
//    }


    public byte[] blobToByte(Blob item) throws Exception {
        byte[] content = null;
        try {
            Blob blob = (Blob) item;
            content = blob.getBytes(0, (int) blob.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }



}
