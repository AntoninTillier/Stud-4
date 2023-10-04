package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class Reducer2 extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    private final FloatWritable ZERO = new FloatWritable(0);
    private final FloatWritable ONE = new FloatWritable(1);

    public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
        String[] keyValues = key.toString().split("/");
        String year = keyValues[0];
        String courseSemester = keyValues[1];
        String numEtu = keyValues[2];
        String newKey = year + "/" + courseSemester;
        key.set(newKey);
        float note = 0;
        int count = 0;
        for (FloatWritable val : values) {
            note += val.get();
            count++;
        }
        if (count >= 1) {
            float resu = note / count;
            if (resu >= 10) {
                context.write(key, ONE);
            } else {
                context.write(key, ZERO);
            }
        }
    }
}