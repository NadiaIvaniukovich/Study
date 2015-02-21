package fruit.juices.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by Hope on 2/18/2015.
 */
public class Juice {
    private List<String> components;

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public int contains(Juice juice) {
        List<String> component = juice.getComponents();
        for (int i = 0; i < component.size(); i++) {
            if (!components.contains(component.get(i))) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return components.toString();
    }
}
