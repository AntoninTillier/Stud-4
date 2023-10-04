package bdma.bigdata.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

public class Numbers {

    public static void main(String[] args) throws IOException {
        Configuration config = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(config);
        try (Admin admin = connection.getAdmin()) {
            TableName tableName = TableName.valueOf("num");
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
            ColumnFamilyDescriptorBuilder columnaDescriptorBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes("a"));
            ColumnFamilyDescriptorBuilder columnbDescriptorBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes("b"));
            tableDescriptorBuilder.setColumnFamily(columnaDescriptorBuilder.build());
            tableDescriptorBuilder.setColumnFamily(columnbDescriptorBuilder.build());
            tableDescriptorBuilder.build();
            admin.createTable(tableDescriptorBuilder.build());
            TableName[] tables = admin.listTableNames();
            if (tables.length != 1 && tableName.equals(tables[0])) {
                throw new IOException("Failed to create table");
            }
            try (Table table = connection.getTable(tableName)) {
                for (int i = 1; i <= 10000; i++) {
                    byte[] row = Bytes.toBytes("row" + i);
                    Put put = new Put(row);
                    byte[] columnFamilyA = Bytes.toBytes("a");
                    byte[] columnFamilyB = Bytes.toBytes("b");
                    if (i % 2 == 1) {
                        for (int j = 0; j < 3; j++) {
                            byte[] qualifier = Bytes.toBytes(caseQualifier(j, 'a'));
                            int nombreAleatoire = (int) (Math.random() * 101);
                            byte[] value = Bytes.toBytes("" + nombreAleatoire);
                            put.addColumn(columnFamilyA, qualifier, value);
                        }
                    } else {
                        for (int j = 0; j < 2; j++) {
                            byte[] qualifier = Bytes.toBytes(caseQualifier(j, 'b'));
                            int nombreAleatoire = (int) (Math.random() * 11);
                            byte[] value = Bytes.toBytes("" + nombreAleatoire);
                            put.addColumn(columnFamilyB, qualifier, value);
                        }
                    }
                    table.put(put);
                }
            }
        }
    }

    public static String caseQualifier(int i, char c) {
        String res = "";
        if (c == 'a') {
            if (i == 0) {
                res = "x";
            } else if (i == 1) {
                res = "y";
            } else {
                res = "z";
            }
        } else {
            if (i == 0) {
                res = "m";
            } else {
                res = "n";
            }
        }
        return res;
    }
}