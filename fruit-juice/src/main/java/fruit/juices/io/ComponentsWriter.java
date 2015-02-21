package fruit.juices.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hope on 2/18/2015.
 */
public class ComponentsWriter {
    private String fileName;

    public ComponentsWriter(String fileName) {
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void write(List<String> stringList) throws IOException{
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
            for (int i = 0; i < stringList.size(); i++){
                bw.write(stringList.get(i)+" ");
            }
        }
    }
}
