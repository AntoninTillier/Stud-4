package bdma.bigdata.firstcount;

import org.apache.hadoop.util.ToolRunner;

public class FirstCount {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new FirstCountDriver(), args);
        System.exit(exitCode);
    }
}