package bdma.bigdata.wordcountline;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountLineMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String s = value.toString();
        for (String w : s.split(" ")) {
            if (w.length() > 0) {
                context.write(new Text(key + ": " + w), new IntWritable(1));
            }
        }
    }
}