package drago.rtc.shape;

import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class OBJReaderTest {

    @Test
    void ignoringUnrecognisedLines() {
        StringReader sr = new StringReader(
        "There was a young lady named Bright\n" +
            "who traveled much faster than light.\n" +
            "She set out one day\n" +
            "\n" +
            "in a relative way,\n" +
            "and came back the previous night.\n"
        );

        OBJReader objReader = new OBJReader(sr);
        objReader.parse();

        assertEquals(6, objReader.linesRead());
        assertEquals(6, objReader.linesIgnored());
    }

    @Test
    void vertextRecords() {
        StringReader sr = new StringReader(
        "v -1 1 0\n" +
            "v -1.0000 0.5000 0.0000\n" +
            "v 1 0 0\n" +
            "v 1 1 0\n"
        );

        OBJReader objReader = new OBJReader(sr);
        objReader.parse();

        assertEquals(Tuple.point(-1, 1, 0), objReader.vertices(1));
        assertEquals(Tuple.point(-1, 0.5, 0), objReader.vertices(2));
        assertEquals(Tuple.point(1, 0, 0), objReader.vertices(3));
        assertEquals(Tuple.point(1, 1, 0), objReader.vertices(4));
    }

    @Test
    void parsingTrianlgeFaces() {
        StringReader sr = new StringReader(
            "v -1 1 0\n" +
                "v -1 0 0\n" +
                "v 1 0 0\n" +
                "v 1 1 0\n" +
                "\n" +
                "f 1 2 3\n" +
                "f 1 3 4\n"
        );

        OBJReader objReader = new OBJReader(sr);
        objReader.parse();

        Group g = objReader.getDefaultGroup();
        Shape s1 = g.getChildren().get(0);
        Shape s2 = g.getChildren().get(1);

        assertTrue(s1 instanceof Triangle);
        assertTrue(s2 instanceof Triangle);

        Triangle t1 = (Triangle) s1;
        Triangle t2 = (Triangle) s2;

        assertEquals(objReader.vertices(1), t1.getPoint1());
        assertEquals(objReader.vertices(2), t1.getPoint2());
        assertEquals(objReader.vertices(3), t1.getPoint3());

        assertEquals(objReader.vertices(1), t2.getPoint1());
        assertEquals(objReader.vertices(3), t2.getPoint2());
        assertEquals(objReader.vertices(4), t2.getPoint3());
    }

    @Test
    void triangulatingPolygons() {
        StringReader sr = new StringReader(
            "v -1 1 0\n" +
                "v -1 0 0\n" +
                "v 1 0 0\n" +
                "v 1 1 0\n" +
                "v 0 2 0\n" +
                "f 1 2 3 4 5\n"
        );

        OBJReader objReader = new OBJReader(sr);
        objReader.parse();

        Group g = objReader.getDefaultGroup();
        assertEquals(3, g.getChildren().size());

        Triangle t1 = (Triangle) g.getChildren().get(0);
        Triangle t2 = (Triangle) g.getChildren().get(1);
        Triangle t3 = (Triangle) g.getChildren().get(2);

        assertEquals(t1.getPoint1(), objReader.vertices(1));
        assertEquals(t1.getPoint2(), objReader.vertices(2));
        assertEquals(t1.getPoint3(), objReader.vertices(3));
        assertEquals(t2.getPoint1(), objReader.vertices(1));
        assertEquals(t2.getPoint2(), objReader.vertices(3));
        assertEquals(t2.getPoint3(), objReader.vertices(4));
        assertEquals(t3.getPoint1(), objReader.vertices(1));
        assertEquals(t3.getPoint2(), objReader.vertices(4));
        assertEquals(t3.getPoint3(), objReader.vertices(5));
    }

    @Test
    void trianlgeInGroups() throws FileNotFoundException {
        FileReader fr = new FileReader("src/fixture/triangles.obj");

        OBJReader objReader = new OBJReader(fr);
        objReader.parse();

        Group g1 = objReader.getGroup("FirstGroup");
        assertNotNull(g1);
        Group g2 = objReader.getGroup("SecondGroup");
        assertNotNull(g2);

        Triangle t1 = (Triangle) g1.getChildren().get(0);
        Triangle t2 = (Triangle) g2.getChildren().get(0);

        assertEquals(t1.getPoint1(), objReader.vertices(1));
        assertEquals(t1.getPoint2(), objReader.vertices(2));
        assertEquals(t1.getPoint3(), objReader.vertices(3));
        assertEquals(t2.getPoint1(), objReader.vertices(1));
        assertEquals(t2.getPoint2(), objReader.vertices(3));
        assertEquals(t2.getPoint3(), objReader.vertices(4));
    }

    @Test
    void convertingAnOBJFileToAGroup() throws FileNotFoundException {
        FileReader fr = new FileReader("src/fixture/triangles.obj");

        OBJReader objReader = new OBJReader(fr);
        objReader.parse();

        Group g = objReader.toGroup(false);
        assertTrue(g.hasChildren());
        assertFalse(g.getChildren().contains(objReader.getDefaultGroup())); // Default Group has no Children.
        assertTrue(g.getChildren().contains(objReader.getGroup("FirstGroup")));
        assertTrue(g.getChildren().contains(objReader.getGroup("SecondGroup")));
    }

    @Test
    void convertingAnOBJFileToADefaultGroup() {
        StringReader sr = new StringReader(
                "v -1 1 0\n" +
                        "v -1 0 0\n" +
                        "v 1 0 0\n" +
                        "v 1 1 0\n" +
                        "v 0 2 0\n" +
                        "f 1 2 3 4 5\n"
        );

        OBJReader objReader = new OBJReader(sr);
        objReader.parse();

        Group g = objReader.toGroup(false);

        assertTrue(g.getChildren().contains(objReader.getDefaultGroup()));
    }
}