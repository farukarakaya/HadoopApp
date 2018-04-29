package Amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;

public class S3configuration {

    static AWSCredentials credentials = new BasicAWSCredentials(
            "<AWS accesskey>",
            "<AWS secretkey>"
    );
    static AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_2)
            .build();

    public static byte[] getGist(String key) throws IOException{
        byte[] bytes = new byte[1920];
        InputStream in = s3client.getObject("bucketName", key).getObjectContent();
        in.read(bytes);
        return bytes;
    }
}

