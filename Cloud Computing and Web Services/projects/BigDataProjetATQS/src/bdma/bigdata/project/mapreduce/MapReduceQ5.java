package bdma.bigdata.project.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MapReduceQ5 {

    public static void main(String[] args) throws Exception {
        String s1 = "";
        String s2 = "";
        switch (args[0]) {
            case "L1":
                s1 = "/S01";
                s2 = "/S02";
                break;
            case "L2":
                s1 = "/S03";
                s2 = "/S04";
                break;
            case "L3":
                s1 = "/S05";
                s2 = "/S06";
                break;
            case "M1":
                s1 = "/S07";
                s2 = "/S08";
                break;
            case "M2":
                s1 = "/S09";
                s2 = "/S10";
                break;
            default:
                args[0] = "Non renseigné";
                break;
        }
        Configuration config5 = new Configuration();
        config5.set("program", args[0]);
        Scan scan1 = new Scan();
        Filter filter1 = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(args[1]));
        Filter filter2 = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(s1 + "|" + s2));
        FilterList filterList = new FilterList(Operator.MUST_PASS_ALL, filter1, filter2);
        scan1.addColumn(Bytes.toBytes("#"), Bytes.toBytes("G"));
        scan1.setFilter(filterList);
        scan1.setCaching(500);
        scan1.setCacheBlocks(false);
        Job job = new Job(config5);
        job.setJarByClass(MapReduceQ5.class);
        TableMapReduceUtil.initTableMapperJob("21602559:G", scan1, Mapper5.class, Text.class, FloatWritable.class, job);
        job.setReducerClass(Reducer_Avg.class);
        job.setNumReduceTasks(1);
        FileOutputFormat.setOutputPath(job, new Path("/home/hadoop/Workspace/RestServer/Results/resuQ5"));
        boolean end = job.waitForCompletion(true);
        if (!end) throw new IOException("Error with the job");
    }
}