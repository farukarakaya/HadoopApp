package CustomWritables;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FloatArrayPairWritable implements Writable{
    private FloatArrayWritable pair1;
    private FloatArrayWritable pair2;

    public FloatArrayPairWritable() {
    }
    public FloatArrayPairWritable(float[] pair1, float[] pair2) {
        this.pair1 = new FloatArrayWritable(pair1);
        this.pair2 = new FloatArrayWritable(pair2);
    }
    public void readFields(DataInput in) throws IOException {
        pair1.readFields(in);
        pair2.readFields(in);
    }
    public void write(DataOutput out) throws IOException {
        pair1.write(out);
        pair2.write(out);
    }

    public FloatArrayWritable getPair1() {
        return pair1;
    }

    public FloatArrayWritable getPair2() {
        return pair2;
    }
}
