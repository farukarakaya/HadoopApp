import CustomWritables.FloatArrayWritable;
import GIST.*;
import Amazon.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GistCompare {

    public static class GistMapper extends Mapper<Object, Text, IntWritable, FloatArrayWritable> {
        String link;
        private int counter = 0;
        private String input;
        private int GroupCount = 10;
        @Override
        protected void setup(Mapper.Context context) throws IOException, InterruptedException {
            input = context.getConfiguration().get("userInputLink");
            GroupCount = Integer.parseInt(context.getConfiguration().get("groupCount"));
          //  input = "features_gist/2/20000.dat";
        }

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            System.out.println(input);
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                link =itr.nextToken();
                IntWritable keyout = new IntWritable((counter)%GroupCount);
                counter++;
                System.out.println(link);
                byte[] bytes2 = S3configuration.getGist(link, "gist-karakaya-bucket");
                byte[] bytes1 = S3configuration.getGist(input, "gist-karakaya-bucket");
                float[] concated_gists = new float[961];
                System.arraycopy(GISTReader.getFloatArray(bytes1),0,concated_gists,0,480);
                System.arraycopy(GISTReader.getFloatArray(bytes2),0,concated_gists,480,480);
                String[] splits = link.split("/");
                String[] ids = splits[2].split("\\.");
                float id = Integer.parseInt(ids[0]);
                concated_gists[960] = id;
                FloatArrayWritable mapper_out = new FloatArrayWritable(concated_gists);
                System.out.println("key= " + keyout + " value= "+ mapper_out);
                context.write(keyout,mapper_out);
            }
        }
    }

    public static class GistReducer
            extends Reducer<IntWritable,FloatArrayWritable,IntWritable,DoubleWritable> {
        private DoubleWritable result = new DoubleWritable();

        public void reduce(IntWritable key, Iterable<FloatArrayWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            IntWritable outkey;
            for (FloatArrayWritable val : values) {
                float[] pair1 = new float[480];
                float[] pair2 = new float[480];
                System.arraycopy(val.getArray(),0,pair1,0,480);
                System.arraycopy(val.getArray(),480,pair2,0,480);
                result.set(AppGist.sim(pair1,pair2));
                outkey = new IntWritable((int) val.getArray()[960]);
                System.out.println("key= " + outkey + "value= "+ result);
                context.write(outkey, result);
            }
        }
    }

    // Arg 0 -> Input loc
    // Arg 1 -> Output loc
    // Arg 2 -> x/xxxx.dat file as input
    // Arg 3 -> Group Size
    // Arg 4 -> Result bucket name
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("userInputLink", ("features_gist/" + args[2] + ".dat") );
        conf.set("groupCount", args[3]);
        Job job = Job.getInstance(conf, "Gist");
        job.setJarByClass(GistCompare.class);
        job.setMapperClass(GistMapper.class);
        job.setReducerClass(GistReducer.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(FloatArrayWritable.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(DoubleWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        int exitCode = job.waitForCompletion(true) ? 0 : 1;
        // Do combining
        String bucketNameArg = args[4];
        ArrayList<String> objectList = S3configuration.getObjectList(bucketNameArg);
        boolean check = S3configuration.uploadCombinedResults(objectList, bucketNameArg);
        if (check) {
            System.out.println("Nailed it!");
        } else {
            System.out.println("Haydaaa... :(");
        }
        System.exit(exitCode);
    }
}