**Getting Started with HBase**

**1 HBase Shell Basics**

The HBase Shell is the command-line interface to HBase cluster(s) that provides both client and administrative operations.

*Connect to HBase*

Connect to a running instance of HBase using the hbase shell command, located in the bin directory of the HBase install. In this example, some usage and version information that is printed when the start HBase Shell has been omitted. The HBase Shell prompt ends with a > character.
```bash
hbase shell
```

*Display HBase Shell Help Text*

Type help and press Enter, to display some basic usage information for HBase Shell, as well as several example commands. Notice that table names, rows, columns all must be enclosed in quote characters.
Use the create command to create a new table. The table name and at least one ColumnFamily name should be specified.
```bash
create 'test', 'cf'
```

*List Information about a Table*

Use the list command to list information about a table.
```bash
list 'test'
```

*Put Data into a Table*

To put data into a table, use the put command.
```bash
put 'test', 'row1', 'cf:a', 'value1'
put 'test', 'row2', 'cf:b', 'value2'
put 'test', 'row3', 'cf:c', 'value3'
```
Here, we insert three values, one at a time. The first insert is at row1, column cf:a, with a value of value1. Columns in HBase are comprised of a column family prefix, cf in this example, followed by a colon and then a column qualifier suffix, a in this case.

*Scan the Table*

One of the ways to get data from HBase is to scan. Use the scan command to scan the table for data. A scan can be limited, but for now, all data is fetched.
```bash
scan 'test'
```

*Get a Single Row*

To get a single row of data at a time, use the get command.
```bash
get 'test', 'row1'
```

*Disable and Delete a Table*

In order to delete a table or change its settings, as well as in some other situations, the table must be disabled first, using the disable command. A table can be re-enabled using the enable command.
```bash
disable 'test'
enable 'test'
```
To delete (drop) a disabled table, use the drop command.
```bash
disable 'test'
drop 'test'
```

**2 HBase Java Client API**

To compile and run HBase Java Client example programs, the following configuration is necessary to setup Eclipse IDE.

- Create a package bdma.bigdata.hbase.
- Create HBase Java Client files in the bdma.bigdata.hbase package.
    - SimpleClient.java 
    - SimpleCounter.java
    - TableCreator.java
Export the whole project to a JAR file, assume hb.jar in the Workspace directory.

The SimpleClient Java class demonstrates how HBase Java Client API administrate a table. In this example, a table test with a column family data will be first created, then a loop will put data to 3 rows with respect to 3 columns to this table. Finally, if the disableTable and deleteTable methods are uncommented, the table test will be disabled and dropped. Verify the result (without dropping the table) in HBase shell. The SimpleClient should be launched by the following command in the directory that contains hb.jar.
```bash
Linux$ java -cp `hbase classpath `:hb.jar bdma.bigdata.hbase.SimpleClient
Windows> run-hbase hb.jar bdma.bigdata.hbase.SimpleClient
```
**Exercise**
Copy SimpleClient Java class to Numbers and implement the following tasks by the new class Numbers.
- Create a table num with two column families a and b.
- Write 10,000 rows to the table num.
    - Use loop control numbers as row keys.
    - If row key is an odd number, put 3 random numbers between 0 and 100 to columns a:x, a:y, and a:z ; if row key is an even number, put 2 random numbers between 0 to 10 to columns b:m and b:n.

The SimpleCounter Java class demonstrates how to use HBase Java MapReduce API to count the row number of a table. Test the SimpleCounter class with the table num.
```bash
Linux$ java -cp `hbase classpath`:hb.jar bdma.bigdata.hbase.SimpleCounter num
Windows> run-hbase hb.jar bdma.bigdata.hbase.SimpleCounter num
```
The TableCreator Java class demonstrates how to create many HBase tables at once. Test the TableCreator class by creating several different tables with different column families.
```bash
Linux$ java -cp `hbase classpath `:hb.jar bdma.bigdata.hbase.TableCreator
Windows> run-hbase hb.jar bdma.bigdata.hbase.TableCreator
```
**Exercise**
Change TableCreator to load table definitions from a text file, for instance, one table per line, in order to create HBase tables in an optimal way.
```bash
Linux$ java -cp `hbase classpath`:hb.jar bdma.bigdata.hbase.TableCreator tables.txt
Windows> run-hbase hb.jar bdma.bigdata.hbase.TableCreator tables.txt
```