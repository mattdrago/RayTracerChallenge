package drago.rtc.shape;

import drago.rtc.foundations.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJReader {
    private BufferedReader sourceReader;
    private List<Tuple> vertices = new ArrayList<>();
    private Group defaultGroup = new Group();
    private Map<String, Group> groups = new HashMap<>();
    private Group currentGroup = defaultGroup; 

    private int linesRead = 0;
    private int linesIgnored = 0;

    private OBJReader() {
        vertices.add(Tuple.point(0, 0, 0));
    }

    public OBJReader(Reader sourceReader) {
        this();
        this.sourceReader = new BufferedReader(sourceReader);
    }

    public void parse() {
        try {
            String line;
            while ((line = sourceReader.readLine()) != null) {
                linesRead++;

                switch(getCommand(line)) {
                    case 'v':
                        parseVertice(line);
                        break;

                    case 'f':
                        parseFace(line);
                        break;
                        
                    case 'g':
                        changeGroup(line);
                        break;

                    default:
                        linesIgnored++;
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeGroup(String line) {
        String[] lineParts = line.split(" ");

        currentGroup = new Group();
        groups.put(lineParts[1], currentGroup);
    }

    private void parseFace(String line) {
        String[] lineParts = line.split(" ");
        Tuple rootVertex = vertices.get(Integer.parseInt(lineParts[1]));

        for(int i = 2, j = 3; j < lineParts.length; ++i, ++j) {
            currentGroup.addChild(new Triangle(
                    rootVertex,
                    vertices.get(Integer.parseInt(lineParts[i])),
                    vertices.get(Integer.parseInt(lineParts[j]))
            ));
        }
    }

    private char getCommand(String line) {
        if(line != null && line.length() > 0) {
            return line.charAt(0);
        }

        return 0;
    }

    private void parseVertice(String line) {
        String[] lineParts = line.split(" ");
        vertices.add(Tuple.point(Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2]), Double.parseDouble(lineParts[3])));
    }

    int linesIgnored() {
        return linesIgnored;
    }

    int linesRead() {
        return linesRead;
    }

    Tuple vertices(int index) {
        return vertices.get(index);
    }

    Group getDefaultGroup() {
        return defaultGroup;
    }

    Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public Group toGroup() {
        Group g = new Group();

        for (Group loadedGroup : this.groups.values()) {
            g.addChild(loadedGroup);
        }

        if(defaultGroup.hasChildren()) {
            g.addChild(defaultGroup);
        }

        return g;
    }
}
