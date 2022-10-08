package com.laptrinhjava.ShoppingCart.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.laptrinhjava.ShoppingCart.service.AmazonClient;
import com.laptrinhjava.ShoppingCart.service.ProductService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClientImpl implements AmazonClient {
    @Autowired
    private ProductService productService;

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
    public String Split(String imageUrl){
        String[] str = imageUrl.split("/");
        return str[str.length - 1];
    }

    @Override
    public void deleteFile(String fileName) {
        String key = Split(fileName);
        amazonS3.deleteObject(bucketName, key);
    }
}
