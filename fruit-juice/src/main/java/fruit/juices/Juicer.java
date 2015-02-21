package fruit.juices;

import fruit.juices.domain.Juice;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Hope on 2/19/2015.
 */
public class Juicer {

    private List<Juice> juices;

    public Juicer(List<Juice> juices) {
        this.juices = juices;
    }

    public int smartSqueeze() {
        Collections.sort(juices, new Comparator<Juice>() {
            @Override
            public int compare(Juice o1, Juice o2) {
                return -Integer.compare(o1.getComponents().size(), o2.getComponents().size());
            }
        });

        System.out.println("Juices to squeeze:");
        for (int i = 0; i < juices.size(); i++) {
            System.out.println(juices.get(i).getComponents());
        }

        System.out.println("Start squeeze.");
        int cycleCount = 0;

        while (!juices.isEmpty()) {
            juicersCycle(0);
            System.out.println("wash");
            cycleCount++;
        }

        return cycleCount;
    }

    private void juicersCycle(int i) {
        Juice juice = juices.get(i);
        for (int j = i + 1; j < juices.size(); j++) {
            if (juice.contains(juices.get(j)) != 0) {
                System.out.println(juice);
                juices.remove(i);
                juicersCycle(j - 1);
                return;
            }
        }
        System.out.println(juice);
        juices.remove(i);
    }
}
