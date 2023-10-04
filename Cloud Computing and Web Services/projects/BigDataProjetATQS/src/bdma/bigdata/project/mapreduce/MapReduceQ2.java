package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MapReduceQ2 {

    public static void main(String[] args) throws Exception {
        String semestre = "/" + StringUtils.leftPad(args[0], 2, '0') + "/";
        Configuration config = new Configuration();
        Scan scan1 = new Scan();
        scan1.addColumn(Bytes.toBytes("#"), Bytes.toBytes("G"));
        scan1.setCaching(500);
        scan1.setCacheBlocks(false);
        Filter filter = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(semestre));
        scan1.setFilter(filter);
        Job job = new Job(config);
        job.setJarByClass(MapReduceQ2.class);
        TableMapReduceUtil.initTableMapperJob("21602559:G", scan1, Mapper2.class, Text.class, FloatWritable.class, job);
        job.setReducerClass(Reducer2.class);
        job.setNumReduceTasks(1);
        FileOutputFormat.setOutputPath(job, new Path("/home/hadoop/Workspace/RestServer/Results/resuQ2Temp"));
        boolean end = job.waitForCompletion(true);
        if (end) {
            Job job2 = Job.getInstance(config, "JobQ2");
            job2.setMapperClass(Mapper2b.class);
            job2.setReducerClass(Reducer_Avg.class);
            job2.setInputFormatClass(KeyValueTextInputFormat.class);
            job2.setMapOutputKeyClass(Text.class);
            job2.setMapOutputValueClass(FloatWritable.class);
            job2.setNumReduceTasks(1);
            FileInputFormat.addInputPath(job2, new Path("/home/hadoop/Workspace/RestServer/Results/resuQ2Temp"));
            FileOutputFormat.setOutputPath(job2, new Path("/home/hadoop/Workspace/RestServer/Results/resuQ2"));
            end = job2.waitForCompletion(true);
        }
    }
}