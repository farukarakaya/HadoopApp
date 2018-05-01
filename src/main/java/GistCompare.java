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
import java.util.StringTokenizer;

public class GistCompare {
    public static String input="features_gist/2/20000.dat";
    public static class GistMapper
            extends Mapper<Object, Text, IntWritable, FloatArrayWritable> {
        String link;
        private int counter = 0;
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            System.out.println(input);
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                link =itr.nextToken();
                IntWritable keyout = new IntWritable((counter)%10);
                counter++;
                System.out.println(link);
                byte[] bytes2 = S3configuration.getGist(link);
                byte[] bytes1 = S3configuration.getGist(input);
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

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
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
        input = "features_gist/2/" + args[2] + ".dat";
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}