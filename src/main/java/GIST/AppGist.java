package GIST;

import java.io.IOException;

public class AppGist {
    /**
     * similarity method
     *
     * @param gist1
     * @param gist2
     * compare gist vectors by euclidean distance metric
     */
    public static double sim(float[] gist1,float[] gist2){
        if (gist1.length != gist2.length)
            throw new IllegalArgumentException("Vectors have differing lengths");
        double distance = 0;

        for (int i=0; i<gist1.length; i++) {
            double diff = (gist1[i] - gist2[i]);
            distance += (diff * diff);
        }
        return 1-distance;
    }

    /**
     * Main method
     *
     * @param args ignored
     * @throws IOException if the image can't be read
     */
    public static void main(String[] args) throws IOException {


    }
}
