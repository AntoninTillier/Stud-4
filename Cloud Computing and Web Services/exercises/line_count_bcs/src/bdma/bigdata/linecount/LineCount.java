package bdma.bigdata.linecount;

import org.apache.hadoop.util.ToolRunner;

public class LineCount {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new LineCountDriver(), args);
        System.exit(exitCode);
    }
}