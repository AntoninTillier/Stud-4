package bdma.bigdata.wordcountline;

import org.apache.hadoop.util.ToolRunner;

public class WordCountLine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordCountLineDriver(), args);
        System.exit(exitCode);
    }
}