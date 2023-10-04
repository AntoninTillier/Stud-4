package bdma.bigdata.wordcount;

import org.apache.hadoop.util.ToolRunner;

public class WordCount {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordCountDriver(), args);
        System.exit(exitCode);
    }
}