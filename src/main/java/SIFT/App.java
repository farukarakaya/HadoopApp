package SIFT;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.asift.ASIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.io.IOUtils;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;
import org.openimaj.util.pair.Pair;
public class App {
    /**
     * Main method
     *
     * @param args ignored
     * @throws IOException if the image can't be read
     */
    public static void main(String[] args) throws IOException {
        // Read the images from two streams
        MBFImage input_1 = ImageUtilities.readMBF(new URL("https://pbs.twimg.com/profile_images/594201934434836482/MiBIp9ny_400x400.jpg"));
        MBFImage input_2 = ImageUtilities.readMBF(new URL("https://lh3.googleusercontent.com/-LX5r4lhj5cY/AAAAAAAAAAI/AAAAAAAAAEA/edhg1svhJMM/photo.jpg"));
        //MBFImage input_2 = ImageUtilities.readMBF(new URL("http://www.dcofis.com/erikli-su-05lt-pet-sise-12li-239-23-B.jpg"));
        // Prepare the engine to the parameters in the IPOL demo
        final ASIFTEngine engine = new ASIFTEngine(false, 7);

        // Extract the keypoints from both images
        final LocalFeatureList<Keypoint> input1Feats = engine.findKeypoints(input_1.flatten());
        System.out.println("Extracted input1: " + input1Feats.size());
        final LocalFeatureList<Keypoint> input2Feats = engine.findKeypoints(input_2.flatten());
        System.out.println("Extracted input2: " + input2Feats.size());

        // Prepare the matcher, uncomment this line to use a basic matcher as
        // opposed to one that enforces homographic consistency
        // LocalFeatureMatcher<Keypoint> matcher = createFastBasicMatcher();
        final LocalFeatureMatcher<Keypoint> matcher = createFastBasicMatcher();

        // Find features in image 1
        matcher.setModelFeatures(input1Feats);
        // ... against image 2
        matcher.findMatches(input2Feats);

        // Get the matches
        final List<Pair<Keypoint>> matches = matcher.getMatches();
        System.out.println("NMatches: " + matches.size());

        // Display the results
        //final MBFImage inp1MBF = input_1.toRGB();
        //final MBFImage inp2MBF = input_2.toRGB();
        IOUtils.writeASCII(System.out, input1Feats);
        DisplayUtilities.display(MatchingUtilities.drawMatches(input_1, input_2, matches, RGBColour.RED));
    }

    /**
     * @return a matcher with a homographic constraint
     */
    private static LocalFeatureMatcher<Keypoint> createConsistentRANSACHomographyMatcher() {
        final ConsistentLocalFeatureMatcher2d<Keypoint> matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                createFastBasicMatcher());
        matcher.setFittingModel(new RobustHomographyEstimator(1.0, 1000, new RANSAC.BestFitStoppingCondition(),
                HomographyRefinement.NONE));

        return matcher;
    }

    /**
     * @return a basic matcher
     */
    private static LocalFeatureMatcher<Keypoint> createFastBasicMatcher() {
        return new FastBasicKeypointMatcher<Keypoint>(5);
    }
}
