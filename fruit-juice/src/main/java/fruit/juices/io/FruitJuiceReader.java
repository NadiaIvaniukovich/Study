package fruit.juices.io;

import fruit.juices.domain.Juice;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Hope on 2/18/2015.
 */
public class FruitJuiceReader {
    private String fileName;
    private List<String> fruits;
    private List<Juice> juices;

    public FruitJuiceReader(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getFruits() {
        return fruits;
    }

    public List<Juice> getJuices() {
        return juices;
    }

    public void read() throws IOException {
        try (FileInputStream in = new FileInputStream(fileName);
             Scanner scanner = new Scanner(in)
        ) {
            fruits = new ArrayList<>();
            juices = new ArrayList<>();

            while (scanner.hasNext()) {
                String[] row = scanner.nextLine().split(" ");
                List<String> components = new ArrayList<>();
                Juice juice = new Juice();
                for (int i = 0; i < row.length; i++) {
                    if (!fruits.contains(row[i])) {
                        fruits.add(row[i]);
                    }

                    components.add(row[i]);
                }

                juice.setComponents(components);
                juices.add(juice);
            }

        }
    }
}
