package com.laptrinhjava.ShoppingCart.utils.awss3util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.laptrinhjava.ShoppingCart.utils.awss3util.IAmazonClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class AmazonClientImpl implements IAmazonClient {
//    @Autowired
//    private IProductService productService;

    private AmazonS3 amazonS3;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;


    /* @PostConstruct để chạy phương thức này sau khi hàm tạo sẽ được gọi bởi vì các trường được đánh dấu với @Value có
      giá trị null trong hàm tạo
       initializeAmazon() là một phương thức để đặt thông tin đăng nhập amazon cho ứng dụng
     */
    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("ap-southeast-1")
                .build();
    }

    // AWS s3 yêu cầu tham số kiểu File, nhưng dữ liệu truyền lên là MultipartFile nên phải chuyển sang File
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        String fileExtension = FilenameUtils.getExtension(multiPart.getOriginalFilename());
        return new Date().getTime() + "." + fileExtension;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        // PublicRead: bất kì ai nếu có đường dẫn của file là có thể truy cập file đó
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl =  endpointUrl + "/" + fileName;
//            fileUrl = fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    // tách url lấy keyname
//    public String Split(String imageUrl){
//        String[] str = imageUrl.split("/");
//        return str[str.length - 1];
//    }
    public ArrayList<String> split (String imageUrl){
        ArrayList<String> fileNames = new ArrayList<>(); // luu danh sach key sau khi tach chuoi
        String[] str = imageUrl.split("," ); // tach rieng tung url
        for(String s : str){
            String[] fileName = s.split("/"); // lay key cua tung chuoi sau khi tach
            fileNames.add(fileName[fileName.length -1]);
        }
        return fileNames;
    }

    @Override
    public void deleteFile(String fileNames) {
        ArrayList<String> keys = split(fileNames);
        for(String key: keys){
            amazonS3.deleteObject(bucketName, key);
        }
    }

    @Override
    public String updateFile(String fileName, String newFile) {
//        String key = Split(fileName);
//        amazonS3.putObject(bucketName, key, new File(newFile));
//        return fileName;
        return null;
    }

    @Override
    public void deleteOneFile(String key) {
        amazonS3.deleteObject(bucketName, key);
    }
}
