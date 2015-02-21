package fruit.juices;

import fruit.juices.io.ComponentsWriter;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Hope on 2/19/2015.
 */
public class FruitSorter implements Runnable {
    private List<String> list;

    public FruitSorter(List<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        ComponentsWriter componentsWriter = new ComponentsWriter("target/juice2.out.txt");
        try {
            componentsWriter.write(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
