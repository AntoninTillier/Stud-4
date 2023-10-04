package bdma.bigdata.project.rest.core;

import java.io.IOException;
import java.util.ArrayList;

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

public class Course {
    private String ID;
    private String nom;
    private String annee;
    public float moyenne;
    public float tauxReussite;
    private ArrayList<String> Instructors = new ArrayList<>();

    public Course(String ID, String annee) throws IOException {
        this.ID = ID;
        Configuration config = HBaseConfiguration.create();
        HTable tableS = new HTable(config, "21602559:C");
        Scan scanS = new Scan();
        scanS.addColumn(Bytes.toBytes("#"), Bytes.toBytes("N"));
        scanS.addColumn(Bytes.toBytes("I"), Bytes.toBytes("1"));
        scanS.addColumn(Bytes.toBytes("I"), Bytes.toBytes("2"));
        scanS.addColumn(Bytes.toBytes("I"), Bytes.toBytes("3"));
        scanS.addColumn(Bytes.toBytes("I"), Bytes.toBytes("4"));
        Filter filterS = new RowFilter(CompareOp.EQUAL, new RegexStringComparator(ID));
        scanS.setFilter(filterS);
        ResultScanner scannerS = tableS.getScanner(scanS);
        byte[] nom = null, Instructor1 = null, Instructor2 = null, Instructor3 = null, Instructor4 = null;
        boolean fini = false;
        for (Result result = scannerS.next(); result != null; result = scannerS.next()) {
            String Rowkey = new String(result.getRow());
            int year = 9999 - Integer.valueOf(Rowkey.split("/")[1]);
            if (year < Integer.valueOf(annee)) {
                this.annee = annee;
                int i = 0;
                for (KeyValue kv : result.raw()) {
                    if (i == 0) {
                        this.nom = new String(kv.getValue());
                        i++;
                        fini = true;

                    } else {
                        this.Instructors.add(new String(kv.getValue()));
                    }
                }
                if (fini) {
                    break;
                }
            }
        }
    }


    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public ArrayList<String> getInstructors() {
        return Instructors;
    }

    public void setInstructors(ArrayList<String> instructors) {
        Instructors = instructors;
    }
}