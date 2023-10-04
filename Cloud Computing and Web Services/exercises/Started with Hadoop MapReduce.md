**Getting Started with Hadoop MapReduce**

**1 General Notes**

The Windows version of Hadoop MapReduce Laboratories use Microsoft Windows 10 to run
native Hadoop MapReduce code. An Apache Hadoop binary distribution should be installed
and configured to run in pseudo-distributed mode that allows to run all Hadoop services on
Microsoft Windows. It is, essentially, a cluster consisting of a single node working just like a full Hadoop cluster, where the only key difference (apart from speed) is that the block replication factor is set to 1, since there is only one DataNode available.

The following instructions allow to setup Apache Hadoop on a Windows workstation. Note that:

- The notation [user] stands for the user’s login name of Windows.
- The notation [package] stands for the available Hadoop binary distribution package name,
the current package is ...._etu_hadoop-***.tar.gz.
- The notation [version] stands for the available Hadoop binary distribution version, the
current version is 3.3.0.

1. Open a Windows Terminal (cmd.exe).
2. Switch to user home directory.
```bash
D:\users\[user]>
```
3. Run Hadoop setup script.
```bash
D:\users\[user]> P:\hadoop\hadoop-env.cmd
D:\users\[user]> P:\hadoop\hadoop-setup.cmd setup P:\hadoop\[package]
```
The above instructions will install the Apache Hadoop Distribution to the folder
```bash
D:\users\[user]\hadoop\[version]
```
We assume that in the rest of this Laboratory, and in all rest Laboratories, the working directory is
```bash
D:\users\[user]\hadoop
```

Furthermore, in order to make all instructions simple, we use the prompt
```bash
> some command
```
instead of the displayed prompt
```bash
D:\users\[user]\hadoop> some command
```
Run the following command to start Hadoop:
```bash
P:\hadoop\hadoop-env.cmd 
P:\hadoop\hadoop-setup.cmd start
```
Run the following command to stop Hadoop:
```bash
P:\hadoop\hadoop-env.cmd 
P:\hadoop\hadoop-setup.cmd stop
```
Important note: before running any Hadoop commands in a new Windows Terminal window, the following setup command must be executed:
```bash
P:\hadoop\hadoop-env.cmd
```
Important note: before running any Hadoop commands in a new Windows Terminal window, the following setup command must be executed:
```bash
P:\hadoop\hadoop-env.cmd
```

**2 Using HDFS**

In this exercise, we will begin to get acquainted with Hadoop tools by manipulating files in HDFS, the Hadoop Distributed File System.
The subsystem associated with HDFS in the Hadoop wrapper program is called FsShell, which can be invoked by the command hadoop fs.

1. We can see a help message describing all the commands associated with this subsystem.
```bash
hadoop fs
```

2. This shows us the contents of the root directory in HDFS.
```bash
hadoop fs -ls /
```
If there is no /user directory, create it by the following commande:
```bash
hadoop fs -mkdir /user
```

3. Individual users have a “home” directory under this directory, named after their username our home directory should be /user/[user].
```bash
hadoop fs -ls /user/[user]
```
There are no files, so the command silently exits. This is different than if we ran hadoop fs -ls /foo, which refers to a directory that doesn’t exist and which would display an error message.
Note that the directory structure in HDFS has nothing to do with the directory structure of the local filesystem, they are completely separate namespaces. Anyway, if our home directory does not exist, create it by the following command.
```bash
hadoop fs -mkdir /user/[user]
```

Besides browsing the existing filesystem, another important thing we can (and must) do with FsShell is to upload new data into HDFS.
The following instructions require the data directory inside the D:\users\[user]\hadoop. Create the data directory in Windows Explorer, then download and extract the two sample data files movpref.txt.gz and shakespeare.tar.gz to the data directory. Make a new directory movpref in data, move movpref.txt to the directory movpref.

1. Insert this directory into HDFS.
```bash
hadoop fs -put data/movpref
```
This copies the local movpref directory and its contents into a remote, HDFS directory named /user/[user]/movpref.
2. List the contents of our HDFS home directory.
```bash
hadoop fs -ls /user/[user]
```
We can see an entry for the movpref directory.
3. Now try the same fs -ls command but without a path argument.
```bash
hadoop fs -ls
```
We see the same results. Indeed, if we don’t pass a directory name to the fs command, it assumes that we mean our home directory, i.e. /user/[user].
4. Upload some files to HD
```bash
hadoop fs -mkdir tmp
hadoop fs -put C:\some\path\some\files tmp
```
5. Run the fs -ls command to verify whether the files are in the tmp HDFS directory.

Now let’s view some of the data copied into HDFS.

1. List the contents of the /user/[user]/tmp directory.
```bash
hadoop fs -ls tmp
```
2. Remove the some files or directories in /user/[user]/tmp directory.
```bash
hadoop fs -rm -r tmp/some/files
```
3. Print the last 10 lines of the file movpref to terminal.
```bash
hadoop fs -cat movpref/movpref.txt | tail -10
```
This command is handy for viewing the output of MapReduce programs. Very often, an individual output file of a MapReduce program is very large, making it inconvenient to view the entire file in the terminal. For this reason, it’s often a good idea to pipe the output of the fs -cat command into head, tail, more, or less. Note that when we pipe the output of the fs -cat command to a local UNIX command, the full contents of the file are still extracted from HDFS and sent to the local machine. Once on the local machine, the file contents are then modified before being displayed.
4. To download a file and manipulate it in the local filesystem, we can use the fs -get command. This command takes two arguments, a HDFS path and a local path, in order to copy the HDFS contents into the local filesystem.
```bash
hadoop fs -get movpref/movpref.txt data/test.txt
type test.txt
```
There are several other commands associated with the FsShell subsystem, to perform most common filesystem manipulations like mv, cp, etc.


**Running MapReduce Jobs**

In this exercise, we will compile a ready-to-run local copy of WordCount Java source files, create a JAR file, and run it as a MapReduce job. It is necessary to create a new directory Code in the directory D:\users\[user]\hadoop. Download the WordCount example code and extract all files to the Code directory.

In addition to manipulating files in HDFS, the wrapper program hadoop is used to launch MapReduce jobs. The code for a job is contained in a compiled JAR file. Hadoop loads the JAR into HDFS and distributes it to the worker nodes, where the individual tasks of the MapReduce job are executed. One simple example of a MapReduce job is to count the number of occurrences of each word in a file or set of files (the famous WordCount program). We will compile and submit such a MapReduce job to count the number of occurrences of every word in the works of Shakespeare (a copy of shakespeare should have been uploaded to HDFS).
1. Switch to WordCount local directory, and the remainder commands are assumed being
executed in this directory.
```bash
cd Code\WordCount
```
2. Compile the Java source code.
```bash
javac -classpath `hadoop classpath` bdma/bigdata/wordcount/*.java
```
Note: in the command above, the quotes around hadoop classpath are backquotes, this runs the hadoop classpath command and uses its output as part of the javac command.
3. Collect compiled Java classes into a JAR file.
```bash
jar cvf wc.jar bdma/bigdata/wordcount/*.class
```
4. Go back to the hadoop working directory.
```bash
copy wc.jar ..\..
cd ..\..
```
To build the WordCount job within an IDE (such as IntelliJ, Eclipse, NetBeans, etc.):
- Create a new project named WordCount.
- Import the source files.
- Add at least the following JAR files to the project class path, which can be found in the share directory in the Hadoop distribution.
- Build the JAR file wc.jar.
```bash
findbugs -annotations hadoop-annotations
hadoop -common
hadoop -mapreduce -client -core
```

To submit and run the WordCount job:

1. Submit a MapReduce job to Hadoop using the JAR file to count the occurrences of each word in Shakespeare.
```bash
hadoop jar wc.jar bdma.bigdata.wordcount.WordCount shakespeare out
```
This hadoop jar command names the JAR file to use (wc.jar), the class whose main method should be invoked (WordCount), and the HDFS input and output directories to use for the MapReduce job. The job reads all the files in the HDFS shakespeare directory, and places its output in a new HDFS directory called out.
2. Run this same command again without any change.
```bash
hadoop jar wc.jar bdma.bigdata.wordcount.WordCount shakespeare out
```
The job halts right away with an exception, because Hadoop automatically fails if a job tries to write its output into an existing directory. This is by design: since the result of a MapReduce job may be expensive to reproduce, Hadoop tries to prevent users from accidentally overwriting previously existing files.
3. Review the result of our first MapReduce job.
```bash
hadoop fs -ls out
```
This lists the output files for a job. Our job ran with only one Reducer, so there should be one file, named part-r-00000, along with a _SUCCESS file and a _logs directory.
4. View the contents of the output for our job.
```bash
hadoop fs -cat out/part-r-00000 | less
```
We can page through a few screens to see words and their frequencies in the works of Shakespeare. Note that we could have specified out/* just as well in this command.
5. Run the WordCount job against files.
```bash
hadoop jar wc.jar bdma.bigdata.wordcount.WordCount shakespeare/*p* outp
```
When the job completes, inspect the contents of the outf directory.
6. Clean up the output files produced by job runs.
```bash
hadoop fs -rm -r out outp
```

A preconfigured installation of Eclipse IDE can be run from the Desktop icon. By default, an improved version of the above WordCount example is presented as the main project. Try to understand the project layout and setup in order to be able to use it as a template to create other MapReduce projects.
- Build a JAR file for the WordCount project in Eclipse IDE and run it in a terminal.
- Create a new Eclipse project, named WordCountPlus with respect to the current project, where the main class should be WordCountPlus instead of WordCount.
- Add a new command line option to WordCountPlus for a minimum word length so the job
should be run as following.
- Implement WordCountPlus that count the occurrence number of each word longer or equals to (≥) the given minimum length.
- Test WordCountPlus with the Shakespeare dataset.
```bash
hadoop jar wcp.jar bdma.bigdata.wordcount.WordCountPlus 3 shakespeare out
```








