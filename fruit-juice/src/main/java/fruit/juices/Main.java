package fruit.juices;

import fruit.juices.io.ComponentsWriter;
import fruit.juices.io.FruitJuiceReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hope on 2/18/2015.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FruitJuiceReader fruitJuiceReader = new FruitJuiceReader("src/main/resources/juices.txt");
        fruitJuiceReader.read();
        List<String> fruits = fruitJuiceReader.getFruits();

        ComponentsWriter componentsWriter = new ComponentsWriter("target/juice1.out.txt");
        componentsWriter.write(fruits);

        Thread thread = new Thread(new FruitSorter(fruits));
        thread.start();

        Juicer juicer = new Juicer(fruitJuiceReader.getJuices());
        Integer cyclesCount = juicer.smartSqueeze();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("target/juice3.out.txt"))) {
            writer.write(cyclesCount.toString());
        }

    }
}
