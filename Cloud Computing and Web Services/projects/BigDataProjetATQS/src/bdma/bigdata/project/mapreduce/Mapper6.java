package bdma.bigdata.project.mapreduce;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Mapper6 extends TableMapper<Text, FloatWritable> {
    public static final byte[] hashtag = "#".getBytes();
    public static final byte[] G = "G".getBytes();
    private final FloatWritable ZERO = new FloatWritable(0);
    private final FloatWritable ONE = new FloatWritable(1);
    private Text text = new Text();
    ArrayList<String> Courses = new ArrayList<>();

    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        String Instructor = conf.get("Instructor");
        Courses = getCourseIntName(Instructor);
    }

    public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
        String[] keyValues = new String(value.getRow()).split("/");
        String year = keyValues[0];
        String courseSemester = keyValues[1];
        String numEtu = keyValues[2];
        String CourseID = keyValues[3];
        String courseYear = CourseID + "/" + year;
        if (Courses.contains(courseYear)) {
            String CourseName = getNom(year, CourseID);
            text.set(year + "/" + CourseID + "/" + CourseName);
            if ((Float.valueOf(new String(value.getValue(hashtag, G))) / 100) >= 10) {
                context.write(text, ONE);
            } else {
                context.write(text, ZERO);
            }
        }
    }

    public static String getNom(String a, String id) throws IOException {
        Configuration config = HBaseConfiguration.create();
        HTable tableS = new HTable(config, "21602559:C");
        Scan scanS = new Scan();
        scanS.addColumn(Bytes.toBytes("#"), Bytes.toBytes("N"));
        Filter filterS = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(id));
        scanS.setFilter(filterS);
        ResultScanner scannerS = tableS.getScanner(scanS);
        String nom = null;
        for (Result result = scannerS.next(); result != null; result = scannerS.next()) {
            String Rowkey = new String(result.getRow());
            int year = 9999 - Integer.valueOf(Rowkey.split("/")[1]);
            if (year <= Integer.valueOf(a)) {
                int i = 0;
                for (KeyValue kv : result.raw()) {
                    if (i == 0) {
                        nom = new String(kv.getValue());
                        return nom;
                    }
                }
            }
        }
        return nom;
    }

    public static ArrayList<String> getCourseIntName(String name) throws IOException {
        Configuration config = HBaseConfiguration.create();
        HTable tableS = new HTable(config, "21602559:C");
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes("I"), Bytes.toBytes("1"));
        scan.addColumn(Bytes.toBytes("I"), Bytes.toBytes("2"));
        scan.addColumn(Bytes.toBytes("I"), Bytes.toBytes("3"));
        scan.addColumn(Bytes.toBytes("I"), Bytes.toBytes("4"));
        Filter filter = new ValueFilter(CompareOp.EQUAL, new RegexStringComparator(name));
        scan.setFilter(filter);
        ResultScanner scanner = tableS.getScanner(scan);
        ArrayList<String> CouseIds = new ArrayList<>();
        for (Result result = scanner.next(); result != null; result = scanner.next()) {
            String[] keyValues = new String(result.getRow()).split("/");
            String CourseId = keyValues[0];
            String yearInv = keyValues[1];
            int year = 9999 - Integer.valueOf(yearInv);
            String CourseYear = CourseId + "/" + year;
            CouseIds.add(CourseYear);
        }
        return CouseIds;
    }
}