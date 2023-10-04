package bdma.bigdata.project.mapreduce;

import java.io.IOException;

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
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Mapper3 extends TableMapper<Text, FloatWritable> {
    public static final byte[] Hashtag = "#".getBytes();
    public static final byte[] G = "G".getBytes();
    private final FloatWritable ZERO = new FloatWritable(0);
    private final FloatWritable ONE = new FloatWritable(1);
    private Text text = new Text();

    public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
        String[] keyValues = new String(value.getRow()).split("/");
        String year = keyValues[0];
        String courseSemester = keyValues[1];
        String numEtu = keyValues[2];
        String CourseID = keyValues[3];
        String nom = getNom(year, CourseID);
        text.set(nom);
        if ((Float.parseFloat(new String(value.getValue(Hashtag, G))) / 100) >= 10) {
            context.write(text, ONE);
        } else {
            context.write(text, ZERO);
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
}