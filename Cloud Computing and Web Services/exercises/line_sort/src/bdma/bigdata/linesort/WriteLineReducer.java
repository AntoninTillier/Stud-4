package bdma.bigdata.linesort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WriteLineReducer extends Reducer<LongWritable, Text, LongWritable, Text> {

    @Override
    public void reduce(LongWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        String s = "";
        for (Text value : values) {
            s = value.toString();
        }
        context.write(key, new Text(s));
    }
}