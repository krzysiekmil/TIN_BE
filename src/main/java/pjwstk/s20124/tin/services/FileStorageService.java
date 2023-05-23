package pjwstk.s20124.tin.services;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

import static pjwstk.s20124.tin.configuration.StaticResourcesWebConfiguration.FILE_DIR;

@Slf4j
@Service
public class FileStorageService {

    private static final String UNDERSCORE = "_";
    private static final String DELIMITER = "/";
    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        log.info("Verify if directory for static content exist if not create it.");
        String filePath = new StringJoiner("/").add(System.getProperty("user.home")).add(FILE_DIR).toString();
        File dir = new File(filePath);
        if(!dir.exists()){
            log.info("Create new directory with path: " + filePath);
            dir.mkdir();
        }
    }

    public String store(MultipartFile file){
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString();
        String filePath = new StringJoiner(DELIMITER).add(System.getProperty("user.home")).add(FILE_DIR).toString();

        filePath = filePath + DELIMITER + fileName + extension;

        try (FileOutputStream fos = new FileOutputStream(filePath,true)){
            fos.write(file.getBytes());
            return fileName + extension;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String store(MultipartFile file, String name){
        String fileName = new StringJoiner("/").add(System.getProperty("user.home")).add(FILE_DIR).add(name).toString();
        try (FileOutputStream fos = new FileOutputStream(fileName,true)){
            fos.write(file.getBytes());
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    public void deleteFile(String name){

        if(Objects.isNull(name) || name.isEmpty()){
            return;
        }

        String filePath = new StringJoiner(DELIMITER).add(System.getProperty("user.home")).add(FILE_DIR).add(name).toString();

        File file = new File(filePath);

        if(file.exists()){
            file.delete();
        }
    }
}
