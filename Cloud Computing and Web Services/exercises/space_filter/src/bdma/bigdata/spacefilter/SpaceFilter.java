package bdma.bigdata.spacefilter;

import org.apache.hadoop.util.ToolRunner;

public class SpaceFilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new SpaceFilterDriver(), args);
        System.exit(exitCode);
    }
}