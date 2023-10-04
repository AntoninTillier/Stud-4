package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

public class Mapper1 extends TableMapper<Text, FloatWritable> {
    public static final byte[] Hashtag = "#".getBytes();
    public static final byte[] G = "G".getBytes();
    private Text text = new Text();

    public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
        String[] keyValues = new String(value.getRow()).split("/");
        String year = keyValues[0];
        String courseSemester = keyValues[1];
        String numEtu = keyValues[2];
        String CourseID = keyValues[3];
        String program = context.getConfiguration().get("program");
        String first = "";
        String second = "";
        switch (program) {
            case "L1":
                first = "S01";
                second = "S02";
                break;
            case "L2":
                first = "S03";
                second = "S04";
                break;
            case "L3":
                first = "S05";
                second = "S06";
                break;
            case "M1":
                first = "S07";
                second = "S08";
                break;
            case "M2":
                first = "S09";
                second = "S10";
                break;
            default:
                program = "Non renseigné";
                break;
        }
        if (CourseID.contains(first) || CourseID.contains(second)) {
            text.set(year + "/" + CourseID);
            context.write(text, new FloatWritable(Float.valueOf(new String(value.getValue(Hashtag, G))) / 100));
        }
    }
}