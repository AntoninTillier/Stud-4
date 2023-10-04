package bdma.bigdata.words;

import org.apache.hadoop.util.ToolRunner;

public class Words {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordsDriver(), args);
        System.exit(exitCode);
    }
}