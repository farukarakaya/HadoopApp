package GIST;

import java.io.IOException;
import java.net.URL;

import org.openimaj.feature.FloatFV;
import org.openimaj.feature.FloatFVComparison;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;

import org.openimaj.image.feature.global.Gist;

public class AppGist {

    public static double sim(FImage queryImage,FImage targetImage){
        double sim = 0.0;
        return sim;
    }
    /**
     * Main method
     *
     * @param args ignored
     * @throws IOException if the image can't be read
     */
    public static void main(String[] args) throws IOException {
        FImage i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13,i14,i15;
        //i1 = ImageUtilities.readF(new URL("https://pbs.twimg.com/profile_images/594201934434836482/MiBIp9ny_400x400.jpg"));
        i2 = ImageUtilities.readF(new URL("https://preview.ibb.co/hio03c/796px_Inside_a_wild_type_banana.jpg"));

        i3 = ImageUtilities.readF(new URL("https://image.ibb.co/ieFcGx/elma.jpg"));
        i4 = ImageUtilities.readF(new URL("https://image.ibb.co/gyK7ic/knowledge_graph_logo.png"));//apple gray
        i5 = ImageUtilities.readF(new URL("https://preview.ibb.co/iJfDOc/unnamed.jpg"));//apple black
        i6 = ImageUtilities.readF(new URL("https://preview.ibb.co/kNzUwx/colorful.jpg"));//apple colorful

        i8 = ImageUtilities.readF(new URL("https://image.ibb.co/igHM9H/trump.jpg"));//trump
        i9 = ImageUtilities.readF(new URL("https://image.ibb.co/n54koc/gollum.jpg"));//gollum
        i10 = ImageUtilities.readF(new URL("https://image.ibb.co/foVmuH/andy.jpg"));//andy

        i12 = ImageUtilities.readF(new URL("https://image.ibb.co/gGMruH/lebron.png"));//lebron
        i13 = ImageUtilities.readF(new URL("https://image.ibb.co/fNrPEH/lance_stephenson_1.jpg"));//stephenson
        i14 = ImageUtilities.readF(new URL("https://image.ibb.co/nGsjgx/i.png"));//lebron2
        i15 = ImageUtilities.readF(new URL("https://image.ibb.co/hj76uH/dragic.png"));//dragic
        Gist fsg = new Gist(256, 256);


        fsg.analyseImage(i2);
        FloatFV f2 = fsg.getResponse();

        fsg.analyseImage(i3);
        FloatFV f3 = fsg.getResponse();

        fsg.analyseImage(i4);
        FloatFV f4 = fsg.getResponse();

        fsg.analyseImage(i5);
        FloatFV f5 = fsg.getResponse();

        fsg.analyseImage(i6);
        FloatFV f6 = fsg.getResponse();

        fsg.analyseImage(i8);
        FloatFV f8 = fsg.getResponse();

        fsg.analyseImage(i9);
        FloatFV f9 = fsg.getResponse();

        fsg.analyseImage(i10);
        FloatFV f10 = fsg.getResponse();

        fsg.analyseImage(i12);
        FloatFV f12 = fsg.getResponse();

        fsg.analyseImage(i13);
        FloatFV f13 = fsg.getResponse();

        fsg.analyseImage(i14);
        FloatFV f14 = fsg.getResponse();

        fsg.analyseImage(i15);
        FloatFV f15 = fsg.getResponse();

        double d2 = FloatFVComparison.SUM_SQUARE.compare(f3, f4);
        double d3 = FloatFVComparison.SUM_SQUARE.compare(f6, f3);
        double d4 = FloatFVComparison.SUM_SQUARE.compare(f4, f5);
        double d5 = FloatFVComparison.SUM_SQUARE.compare(f5, f6);
        double d6 = FloatFVComparison.SUM_SQUARE.compare(f5, f3);
        double d7 = FloatFVComparison.SUM_SQUARE.compare(f9, f10);
        double d8 = FloatFVComparison.SUM_SQUARE.compare(f9, f8);
        double d10 = FloatFVComparison.SUM_SQUARE.compare(f12, f13);
        double d11 = FloatFVComparison.SUM_SQUARE.compare(f12, f14);
        double d12 = FloatFVComparison.SUM_SQUARE.compare(f12, f15);//lebron dragic
        double d13 = FloatFVComparison.SUM_SQUARE.compare(f13, f15);//stephenson dragic

        System.out.println("elma - apple gray distance: " + d2);
        System.out.println("elma - colorful distance: " + d3);
        System.out.println("apple gray - apple black distance: " + d4);
        System.out.println("apple black - apple colorful distance: " + d5);
        System.out.println("elma - apple black distance: " + d6);
        System.out.println("gollum - andy distance: " + d7);
        System.out.println("gollum - trump distance: " + d8);
        System.out.println("lebron - stepehson similarity: " + (1- d10) );
        System.out.println("lebron - lebron 2 similarity: " + (1- d11) );
        System.out.println("lebron - dragic similarity: " + (1- d12) );
        System.out.println("stepehenson - dragic similarity: " + (1- d13) );

    }
}
