package bdma.bigdata.project.rest.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.util.Bytes;


public class Student {
    private String name;
    private String mail;
    private String program;
    private Map<String, String> First;
    private Map<String, String> Second;

    public Student(String ID, String semester) throws IOException {
        String program = getProgram(semester);
        Configuration config = HBaseConfiguration.create();
        HTable tableS = new HTable(config, "21602559:S");
        Scan scanS = new Scan();
        scanS.addColumn(Bytes.toBytes("#"), Bytes.toBytes("F"));
        scanS.addColumn(Bytes.toBytes("#"), Bytes.toBytes("L"));
        scanS.addColumn(Bytes.toBytes("#"), Bytes.toBytes("P"));
        scanS.addColumn(Bytes.toBytes("C"), Bytes.toBytes("E"));
        Filter filterS = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(ID));
        Filter filterS2 = new SingleColumnValueFilter(Bytes.toBytes("#"),
                Bytes.toBytes("P"), CompareOp.EQUAL, new RegexStringComparator(program));
        FilterList filterListS = new FilterList(Operator.MUST_PASS_ALL, filterS, filterS2);
        scanS.setFilter(filterListS);
        ResultScanner scannerS = tableS.getScanner(scanS);
        byte[] firstname = null;
        byte[] lastname = null;
        byte[] programS = null;
        byte[] mail = null;
        // Reading values from Result class object
        for (Result result = scannerS.next(); result != null; result = scannerS.next()) {
            if (result.getValue(Bytes.toBytes("#"), Bytes.toBytes("F")) != null)
                firstname = result.getValue(Bytes.toBytes("#"), Bytes.toBytes("F"));
            if (result.getValue(Bytes.toBytes("#"), Bytes.toBytes("L")) != null)
                lastname = result.getValue(Bytes.toBytes("#"), Bytes.toBytes("L"));
            if (result.getValue(Bytes.toBytes("#"), Bytes.toBytes("P")) != null)
                programS = result.getValue(Bytes.toBytes("#"), Bytes.toBytes("P"));
            if (result.getValue(Bytes.toBytes("C"), Bytes.toBytes("E")) != null)
                mail = result.getValue(Bytes.toBytes("C"), Bytes.toBytes("E"));
        }
        // Printing the values
        if (Bytes.toString(firstname) != null) {
            this.name = Bytes.toString(firstname) + " " + Bytes.toString(lastname);
            this.mail = Bytes.toString(mail);
        }
    }

    public static String getProgram(String s) {
        switch (s) {
            case "L1":
                return "1";
            case "L2":
                return "2";
            case "L3":
                return "3";
            case "M1":
                return "4";
            case "M2":
                return "5";
            default:
                return "Non renseigné";
        }
    }

    /*** Getters and Setters ***/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Map<String, String> getFirst() {
        return First;
    }

    public void setFirst(Map<String, String> first) {
        First = first;
    }

    public Map<String, String> getSecond() {
        return Second;
    }

    public void setSecond(Map<String, String> second) {
        Second = second;
    }
}