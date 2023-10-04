package bdma.bigdata.linesort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String a = "a z e r t y u i o";
        String[] tab = a.split(" ");
        List<String> al = new ArrayList<String>();
        al = Arrays.asList(tab);
        Collections.sort(al);
        Object[] tabb = al.toArray();
        for (int i = 0; i < tab.length; i++) {
            System.out.print(tab[i]);
        }
        System.out.println();
    }
}