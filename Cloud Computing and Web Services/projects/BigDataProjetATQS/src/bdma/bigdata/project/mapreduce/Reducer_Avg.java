package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer_Avg extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
        float note = 0;
        int count = 0;
        for (FloatWritable val : values) {
            note += val.get();
            count++;
        }
        if (count >= 1) {
            float resu = note / count;
            context.write(key, new FloatWritable(resu));
        }
    }
}