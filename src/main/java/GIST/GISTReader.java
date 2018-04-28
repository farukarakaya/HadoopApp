package GIST;

import org.openimaj.ml.clustering.FloatCentroidsResult;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class GISTReader {
    public static float[] getFloatArr(Path path){
        byte[] bytes =Files.readAllBytes(path);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        float[] fl = new float[480];

        for(int i = 0; i < 1920; i += 4){

            fl[i/4] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, i, i+4)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        }
        return fl;
    }
    public static void main(String[]args){
        try {
            Path path = Paths.get("/home/ofk/Documents/cs425/HadoopApp/0.dat");
            byte[] bytes =Files.readAllBytes(path);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            float[] fl = new float[480];

            for(int i = 0; i < 1920; i += 4){

                fl[i/4] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, i, i+4)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            }
            System.out.println(fl.length);
        }catch (IOException e){
            System.out.println("sadsdas");
        }
    }

}
