package kr.co.metasoft.groupware.api.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.metasoft.groupware.api.app.dto.SealImageDto;
import kr.co.metasoft.groupware.api.common.dto.UserSealDto;

@Service
public class SealSaveService{


    @Transactional
    public UserSealDto createSealDto(SealImageDto sealImageDto , Long userId) {

        MultipartFile sealImage = sealImageDto.getSealImage();

        MultipartFile signImage = sealImageDto.getSignImage();

        if(sealImage != null && sealImage.getSize() > 0 && sealImage.getOriginalFilename() != null) {
            System.out.println("sealImage : "+ sealImage);
            System.out.println("sealImage 파일이름 : "+ sealImage.getOriginalFilename());
            System.out.println("sealImage 사이즈 : "+ sealImage.getSize());
        }else {
            System.out.println("sealImage : "+ sealImage);
            System.out.println("sealImage 파일이름 : "+ sealImage.getOriginalFilename());
            System.out.println("sealImage 사이즈 : "+ sealImage.getSize());
        }
        if(signImage != null && signImage.getSize() > 0 && signImage.getOriginalFilename() != null) {
            System.out.println("signImage : "+ signImage);
            System.out.println("signImage 파일이름: "+ signImage.getOriginalFilename());
            System.out.println("signImage 사이즈 : "+ signImage.getSize());
        }else {
            System.out.println("signImage : "+ signImage);
            System.out.println("signImage 파일이름: "+ signImage.getOriginalFilename());
            System.out.println("signImage 사이즈 : "+ signImage.getSize());
        }

//        Long userId = sealImageDto.getUserId();

        //파일 변환
        File sealFile = convertMultiFileToFile(sealImage);
        File signFile = convertMultiFileToFile(signImage);

        //파일 -> Blob
        Blob sealBlob = convertFileToBlob(sealFile);
        Blob signBlob = convertFileToBlob(signFile);


        UserSealDto sealDto = UserSealDto.builder()
                .userId(userId)
                .sealImage(sealBlob)
                .signImage(signBlob)
                .build();

        return sealDto;
    }
    public Blob convertFileToBlob(File file) {
        Blob blob = null;
        FileInputStream inputStream = null;
        try {
            byte[] byteArray = new byte[(int)file.length()];
            inputStream = new FileInputStream(file);
            inputStream.read(byteArray);

            blob = new javax.sql.rowset.serial.SerialBlob(byteArray);
        } catch (Exception e) {
            System.out.println("Blob convert 오류");
        }
        finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
                inputStream = null;
            }finally {
                inputStream = null;
            }
        }
        return blob;

    }



    public File convertMultiFileToFile(MultipartFile mFile) {
        File convertFile = new File(mFile.getOriginalFilename());
        try {
            convertFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(mFile.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 convert에서 오류 ");
        }
        return convertFile;
    }


}
