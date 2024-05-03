package com.smart.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileHelper {
    //public final String UPLOAD_DIR = System.getProperty("user.dir") + "/images/"; /*new ClassPathResource("static/image").getFile().getPath();*/

    //String path = UPLOAD_DIR.replace("\\","/");

    public FileHelper() throws IOException{

    }
  /*  //public final String UPLOAD_DIR="D:\\workspace-spring-tool-suite-4-4.16.0.RELEASE\\SpringBoot_PostSql-1\\src\\main\\resources\\static\\image";
    public boolean uploadFile(MultipartFile fileData){
        boolean f = false;
        try{
            InputStream inputStream = fileData.getInputStream();
            byte data[] = new byte[inputStream.available()];

            inputStream.read(data);

            FileOutputStream fos = new FileOutputStream(UPLOAD_DIR+"/"+fileData.getOriginalFilename());

            fos.write(data);
            fos.close();
            f=true;
            Files.copy(fileData.getInputStream(), Paths.get(UPLOAD_DIR.concat(fileData.getOriginalFilename())));
        }catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }
*/
    //serve the file
    public InputStream getImage(String path, MultipartFile file) throws FileNotFoundException {
        String fullPath = path +"/"+file;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
    public  String uploadImage(String path, MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();
        String fileName =randomId.concat(name.substring(name.lastIndexOf(".")));

        String filePath = path+ File.separator + fileName;

        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return  name;
    }
    public InputStream getImage(String path, String fileName) throws FileNotFoundException {
        String fullPath = path+fileName+".jpg";
        fullPath= fullPath.replaceAll("\\/","/");
        System.out.println(fullPath);
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
}

