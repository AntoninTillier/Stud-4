package bdma.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class TableCreator {

    private final TreeMap<String, List<String>> schemaMap;

    private TableCreator() {
        schemaMap = new TreeMap<>();
        String[] tables = {"T1 CF1 CF2 CF3", "T2 CF1 CF2", "T3 CF1 CF2 CF3 CF4"};
        for (String table : tables) {
            setSchemaMap(table);
        }
    }

    public static void main(String[] args) throws IOException {
        TableCreator schema = new TableCreator();
        schema.createTables();
    }

    private void setSchemaMap(String table) {
        String[] schema = table.split(" ");
        if (schema.length < 2) {
            return;
        }
        String tableName = schema[0];
        List<String> columnFamilies = Arrays.asList(schema);
        this.schemaMap.put(tableName, columnFamilies);
    }

    private void createTables() throws IOException {
        Configuration config = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(config);
        try (Admin admin = connection.getAdmin()) {
            for (String table : schemaMap.keySet()) {
                TableName tableName = TableName.valueOf(table);
                TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
                for (String family : schemaMap.get(table)) {
                    ColumnFamilyDescriptorBuilder columnDescriptorBuilder = ColumnFamilyDescriptorBuilder
                            .newBuilder(Bytes.toBytes(family));
                    tableDescriptorBuilder.setColumnFamily(columnDescriptorBuilder.build());
                    tableDescriptorBuilder.build();
                }
                admin.createTable(tableDescriptorBuilder.build());
            }
            TableName[] tableNames = admin.listTableNames();
            if (tableNames.length == 0) {
                throw new IOException("No table has been created.");
            }
            System.out.println("The following tables have been found in HBase:");
            for (TableName tableName : tableNames) {
                System.out.println(Arrays.toString(tableName.getName()));
            }
        }
    }
}