package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper2b extends Mapper<Text, Text, Text, FloatWritable> {
    public static final byte[] hashtag = "#".getBytes();
    public static final byte[] G = "G".getBytes();
    private Text text = new Text();

    public void map(Text row, Text value, Context context) throws IOException, InterruptedException {
        String[] keyValues = new String(row.getBytes()).split("/");
        String year = keyValues[0];
        String courseSemester = keyValues[1];
        text.set(year);
        context.write(text, new FloatWritable(Float.valueOf((value.toString()))));
    }
}