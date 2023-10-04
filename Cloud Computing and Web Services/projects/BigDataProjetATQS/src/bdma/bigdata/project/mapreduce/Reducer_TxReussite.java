package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer_TxReussite extends Reducer<Text, FloatWritable, Text, FloatWritable> {

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int moyenne = 0;
        int count = 0;
        for (IntWritable value : values) {
            if (value.get() >= 10) {
                moyenne++;
            }
            count++;
        }
        float tauxR = (float) moyenne / count;
        context.write(key, new FloatWritable(tauxR));
    }
}