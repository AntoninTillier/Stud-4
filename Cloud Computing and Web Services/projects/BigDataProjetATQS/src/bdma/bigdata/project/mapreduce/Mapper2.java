package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

public class Mapper2 extends TableMapper<Text, FloatWritable> {
    public static final byte[] Hashtag = "#".getBytes();
    public static final byte[] G = "G".getBytes();
    private Text text = new Text();

    public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
        String[] keyValues = new String(value.getRow()).split("/");
        String year = keyValues[0];
        String courseSemester = keyValues[1];
        String numEtu = keyValues[2];
        String CourseID = keyValues[3];
        text.set(year + "/" + courseSemester + "/" + numEtu);
        context.write(text, new FloatWritable(Float.valueOf(new String(value.getValue(Hashtag, G))) / 100));
    }
}