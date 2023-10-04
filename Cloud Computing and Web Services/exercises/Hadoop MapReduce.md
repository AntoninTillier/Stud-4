**Writing Hadoop MapReduce Programs**

**1 Preparations**

- Create a package bdma.bigdata.hadoop.
- Copy the following 4 files from the WordCount project to current BigData project, in the bdma.bigdata.hadoop package.
    - WordCount.java
    - WordCountDriver.java
    - WordCountMapper.java 
    - WordCountReducer.java
- Rename the class WordCountReducer to SumReducer, which will be a common Reducer for several jobs.

**2 MapReduce Jobs**

Implement all MapReduce jobs studied in the Tutorial (at least 1, 2, 3, 6) and test them with the movpref data. The directory movpref should have been uploaded to the HDFS according to the precedent Laboratory. Furthermore, all commands must be executed in the Workspace directory.

*Line Count*

- Copy the class WordCount to LineCount.
- Copy the class WordCountDriver to LineCountDriver.
- Copy the class WordCountMapper to LineCountMapper.
- Create new class LineCountReducer.
- Implement the LineCount job.
- Export the whole project to a JAR file, assume b.jar in the Workspace directory.
- Run the LineCount job as following.

*Character Count*

- Copy the class WordCount to CharCount.
- Copy the class WordCountDriver to CharCountDriver.
- Copy the class WordCountMapper to CharCountMapper.
- Use the class SumReducer as Reducer.
- Implement the CharCount job.
- Export the whole project to a JAR file, assume b.jar in the Workspace directory.
- Run the CharCount job as following.

*First Letter Count*

- Copy the class WordCount to FirstCount.
- Copy the class WordCountDriver to FirstCountDriver.
- Copy the class WordCountMapper to FirstCountMapper.
- Use the class SumReducer as Reducer.
- Implement the FirstCount job.
- Export the whole project to a JAR file, assume b.jar in the Workspace directory.
- Run the FirstCount job as following.