package bdma.bigdata.firstcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FirstCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String s = value.toString();
        for (String w : s.split(" ")) {
            if (w.length() > 0) {
                String firstLetter = String.valueOf(w.toCharArray()[0]);
                context.write(new Text(firstLetter), new IntWritable(1));
            }
        }
    }
}