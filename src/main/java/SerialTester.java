import Amazon.S3configuration;
import GIST.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class SerialTester {

    public static void main(String[] args) throws IOException {
        float[] query = GISTReader.getFloatArray(Files.readAllBytes(Paths.get("//home/ofk/Documents/cs425/features/features_gist/25/250023.dat")));
        float[] fl = null;
        byte[] bytes;
        double sim;
        Path path;
        for (int i = 1; i < 1000000; i++) {

            //bytes = S3configuration.getGist("features_gist/0/" + i +".dat", "gist-karakaya-bucket");
            path = Paths.get("/home/ofk/Documents/cs425/features/features_gist/" + i / 10000 + "/" + i + ".dat");
            bytes = Files.readAllBytes(path);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            fl = new float[480];
            for (int j = 0; j < 1920; j += 4)
                fl[j / 4] = ByteBuffer.wrap(Arrays.copyOfRange(bytes, j, j + 4)).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            sim = AppGist.sim(query, fl);
            if (sim > 0.7) {
                System.out.println(sim + " " + i);
            }
        }
    }
}
