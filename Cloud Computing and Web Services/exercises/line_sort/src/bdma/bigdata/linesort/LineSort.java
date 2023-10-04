package bdma.bigdata.linesort;

import org.apache.hadoop.util.ToolRunner;

public class LineSort {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new LineSortDriver(), args);
        System.exit(exitCode);
    }
}