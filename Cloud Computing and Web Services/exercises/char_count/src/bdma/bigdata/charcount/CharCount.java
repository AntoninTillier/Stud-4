package bdma.bigdata.charcount;

import org.apache.hadoop.util.ToolRunner;

public class CharCount {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new CharCountDriver(), args);
        System.exit(exitCode);
    }
}