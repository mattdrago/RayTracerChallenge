package drago.rtc.shape;

import drago.rtc.foundations.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJReader {
    private BufferedReader sourceReader;
    private final List<Tuple> vertices = new ArrayList<>();
    private final List<Tuple> vertexNormals = new ArrayList<>();
    private final Group defaultGroup = new Group();
    private final Map<String, Group> groups = new HashMap<>();
    private Group currentGroup = defaultGroup; 

    private int linesRead = 0;
    private int linesIgnored = 0;

    private OBJReader() {
        vertices.add(Tuple.point(0, 0, 0));
        vertexNormals.add(Tuple.vector(0, 0, 0));
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

                switch(Command.getFromLine(line)) {
                    case VERTEX:
                        parseVertice(line);
                        break;

                    case FACE:
                        parseFace(line);
                        break;
                        
                    case GROUP:
                        changeGroup(line);
                        break;

                    case VERTEX_NORMAL:
                        parseVertexNormal(line);
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
        FaceNode rootFaceNode = new FaceNode(lineParts[1]);
        Tuple rootVertex = vertices.get(rootFaceNode.vertexIndex);
        Tuple rootVertexNormal = vertexNormals.get(rootFaceNode.vertexNormalIndex);

        for(int i = 2, j = 3; j < lineParts.length; ++i, ++j) {
            FaceNode faceNodeI = new FaceNode(lineParts[i]);
            FaceNode faceNodeJ = new FaceNode(lineParts[j]);

            Triangle tri;

            if(rootFaceNode.smooth) {
                tri = new SmoothTriangle(rootVertex, vertices.get(faceNodeI.vertexIndex), vertices.get(faceNodeJ.vertexIndex),
                        rootVertexNormal, vertexNormals.get(faceNodeI.vertexNormalIndex), vertexNormals.get(faceNodeJ.vertexNormalIndex));
            } else {
                tri = new Triangle(rootVertex, vertices.get(faceNodeI.vertexIndex), vertices.get(faceNodeJ.vertexIndex));
            }

            currentGroup.addChild(tri);
        }
    }


    private void parseVertice(String line) {
        String[] lineParts = line.split(" ");
        vertices.add(Tuple.point(Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2]), Double.parseDouble(lineParts[3])));
    }

    private void parseVertexNormal(String line) {
        String[] lineParts = line.split(" ");
        vertexNormals.add(Tuple.vector(Double.parseDouble(lineParts[1]), Double.parseDouble(lineParts[2]), Double.parseDouble(lineParts[3])));
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

    Tuple vertexNormal(int index) {
        return vertexNormals.get(index);
    }

    Group getDefaultGroup() {
        return defaultGroup;
    }

    Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public Group toGroup(boolean subdivide) {
        Group g = new Group();

        for (Group loadedGroup : this.groups.values()) {
            doSubdivide(loadedGroup, subdivide);
            g.addChild(loadedGroup);
        }

        if(defaultGroup.hasChildren()) {
            doSubdivide(defaultGroup, subdivide);
            g.addChild(defaultGroup);
        }

        return g;
    }

    private void doSubdivide(Group group, boolean subdivide) {
        if(subdivide) {
            group.subDivide();
            for(Shape s: group.getChildren()) {
                if(s instanceof Group) {
                    ((Group)s).subDivide();
                }
            }
        }
    }

    private enum Command {
        VERTEX("v"),
        FACE("f"),
        GROUP("g"),
        VERTEX_NORMAL("vn"),
        UNKNOWN("");

        private final String code;

        Command(String code) {
            this.code = code;
        }

        static Command getFromLine(String line) {
            if(line != null && line.length() > 0) {
                String[] lineParts = line.split(" ");

                for (Command c : Command.values()) {
                    if (c.code.equals(lineParts[0])) {
                        return c;
                    }
                }
            }

            return UNKNOWN;
        }
    }

    private class FaceNode {

        final int vertexIndex;
        final int textureVertex;
        final int vertexNormalIndex;
        final boolean smooth;

        FaceNode(String linePart) {
            String[] parts = linePart.split("/");
            this.vertexIndex = Integer.parseInt(parts[0]);

            if(parts.length > 1) {
                if(parts[1].length() > 0) {
                    this.textureVertex = Integer.parseInt(parts[1]);
                } else {
                    this.textureVertex = 0;
                }

                this.vertexNormalIndex = Integer.parseInt(parts[2]);
                this.smooth = true;
            } else {
                this.textureVertex = 0;
                this.vertexNormalIndex = 0;
                this.smooth = false;
            }
        }
    }
}
