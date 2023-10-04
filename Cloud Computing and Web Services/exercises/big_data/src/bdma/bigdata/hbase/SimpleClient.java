package bdma.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class SimpleClient {

    public static void main(String[] args) throws IOException {
        Configuration config = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(config);
        try (Admin admin = connection.getAdmin()) {
            TableName tableName = TableName.valueOf("test");
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
            ColumnFamilyDescriptorBuilder columnDescriptorBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes("data"));
            tableDescriptorBuilder.setColumnFamily(columnDescriptorBuilder.build());
            tableDescriptorBuilder.build();
            admin.createTable(tableDescriptorBuilder.build());
            TableName[] tables = admin.listTableNames();
            if (tables.length != 1 && tableName.equals(tables[0])) {
                throw new IOException("Failed to create table");
            }
            try (Table table = connection.getTable(tableName)) {
                for (int i = 1; i <= 3; i++) {
                    byte[] row = Bytes.toBytes("row" + i);
                    Put put = new Put(row);
                    byte[] columnFamily = Bytes.toBytes("data");
                    byte[] qualifier = Bytes.toBytes(String.valueOf(i));
                    byte[] value = Bytes.toBytes("value" + i);
                    put.addColumn(columnFamily, qualifier, value);
                    table.put(put);
                }
                Get get = new Get(Bytes.toBytes("row1"));
                Result result = table.get(get);
                System.out.println("Get: " + result);
                Scan scan = new Scan();
                try (ResultScanner scanner = table.getScanner(scan)) {
                    for (Result scannerResult : scanner) {
                        System.out.println("Scan: " + scannerResult);
                    }
                }
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            }
        }
    }
}