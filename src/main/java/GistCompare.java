import CustomWritables.FloatArrayPairWritable;
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

    public static class GistMapper
            extends Mapper<Object, Text, IntWritable, FloatArrayPairWritable> {
        String link;
        String input="https://s3-eu-west-1.amazonaws.com/gist-karakaya-bucket/features_gist/2/20000.dat";
        private int counter = 0;
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {

            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                link =itr.nextToken();
                IntWritable keyout = new IntWritable((counter)%10);
                counter++;
                byte[] bytes2 = S3configuration.getGist(link);
                byte[] bytes1 = S3configuration.getGist(input);
                FloatArrayPairWritable gist_array= new FloatArrayPairWritable(GISTReader.getFloatArray(bytes1),GISTReader.getFloatArray(bytes2));
                context.write(keyout,gist_array);
            }
        }
    }

    public static class GistReducer
            extends Reducer<IntWritable,FloatArrayPairWritable,IntWritable,DoubleWritable> {
        private DoubleWritable result = new DoubleWritable();

        public void reduce(IntWritable key, Iterable<FloatArrayPairWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (FloatArrayPairWritable val : values) {
                result.set(AppGist.sim(val.getPair1().getArray(),val.getPair2().getArray()));
                context.write(key, result);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Gist");
        job.setJarByClass(GistCompare.class);
        job.setMapperClass(GistMapper.class);;
        job.setReducerClass(GistReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(DoubleWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}