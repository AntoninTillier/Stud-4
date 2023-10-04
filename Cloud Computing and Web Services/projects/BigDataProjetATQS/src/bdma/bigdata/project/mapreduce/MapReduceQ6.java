package bdma.bigdata.project.mapreduce;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MapReduceQ6 {
    public ArrayList<String> Courses;

    public static void main(String[] args) throws Exception {
        Configuration config5 = new Configuration();
        config5.set("Instructor", args[0]);
        Scan scan1 = new Scan();
        scan1.addColumn(Bytes.toBytes("#"), Bytes.toBytes("G"));
        scan1.setCaching(500);
        scan1.setCacheBlocks(false);
        Job job = new Job(config5);
        job.setJarByClass(MapReduceQ5.class);
        TableMapReduceUtil.initTableMapperJob("21602559:G", scan1, Mapper6.class, Text.class, FloatWritable.class, job);
        job.setReducerClass(Reducer_Avg.class);
        job.setNumReduceTasks(1);
        FileOutputFormat.setOutputPath(job, new Path("/home/hadoop/Workspace/RestServer/Results/resuQ6"));
        boolean end = job.waitForCompletion(true);
        if (!end) throw new IOException("Error with the job");
    }
}