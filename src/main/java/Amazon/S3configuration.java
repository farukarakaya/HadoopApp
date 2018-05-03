package Amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class S3configuration {

    static AWSCredentials credentials = new BasicAWSCredentials(
            "AKIAI7Z3XZ6CJUU7P2PA",
            "v7PcCzDvSxJGtMr+xX73ysBqXMeLGSW/1+vubQWY"
    );
    static AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();

    static AWSCredentials credentials2 = new BasicAWSCredentials(
            "AKIAJ3K2RVESIOJZNW3Q",
            "9P0veFKeNJveVzHxto/D9096uY8xTwxYLwJJl8ve"
    );
    static AmazonS3 s3client2 = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials2))
            .build();

    public static byte[] getGist(String key, String bucketName) throws IOException{
        byte[] bytes = new byte[1920];
        //System.out.println(key);
        String newKey = key;
        newKey = newKey.replace("https://s3-eu-west-1.amazonaws.com/gist-karakaya-bucket/features_gist/","");
        InputStream in = s3client2.getObject( bucketName , newKey).getObjectContent();
        in.read(bytes);
        in.close();
        return bytes;
    }

    public static ArrayList<String> getObjectList( String bucketName ){
        ArrayList<String> resultList = new ArrayList<String>();
        ListObjectsV2Result result = s3client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os: objects) {
            String str = os.getKey();
            if ( str.contains("MapReduce/output/part"))
                resultList.add(str);
        }
        return resultList;
    }


    public static boolean uploadCombinedResults(ArrayList<String> allLines, String bucketName) {
        try {
            ArrayList<String> ThresholdedOnes = new ArrayList<String>();
            File dir = new File(".");
            dir.mkdirs();
            File tmp = new File(dir, "BatchResult.txt");
            boolean isCreated = tmp.createNewFile();
            if ( isCreated ) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(tmp.getPath(), true));
                System.out.println("We are here... Printing the content of all files.. ");
                while ( allLines.size() > 0) {
                    System.out.println(allLines.get( allLines.size()-1 ));
                    InputStream in = s3client.getObject( bucketName , allLines.get( allLines.size()-1) ).getObjectContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] arr = line.split(" ");
                        if ( Float.parseFloat(arr[1]) >= 0.9 ) {
                            ThresholdedOnes.add(line);
                        }
                        line += "\n";
                        writer.append(  line );
                    }
                    allLines.remove(allLines.size()-1);
                }
                writer.close();

                File tmpFile = new File(dir, "ThresholdedResults.txt");
                isCreated = tmpFile.createNewFile();
                if (isCreated) {
                    writer = new BufferedWriter(new FileWriter(tmpFile.getPath(), true));
                    for (int f = 0; f < ThresholdedOnes.size(); f++) {
                        String tstr = ThresholdedOnes.get(f) + "\n";
                        writer.append( tstr );
                    }
                    writer.close();
                    s3client.putObject(bucketName, "Results/ThresholdedResults.txt", tmpFile);
                }
                s3client.putObject(bucketName, "Results/BatchResult.txt", tmp);
                return true;
            }
        } catch (Exception e ) {
            System.out.println("ERROR! " + e.toString());
        }

        return false;
    }
}

