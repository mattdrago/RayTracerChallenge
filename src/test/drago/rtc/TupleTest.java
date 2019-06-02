package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    private static final double X = 4.3;
    private static final double Y = -4.2;
    private static final double Z = 3.1;

    @Test
    void tupleWithWEqual1IsAPoint() {
        double w = 1.0;

        Tuple a = new Tuple(X, Y, Z, w);

        assertEquals(X, a.getX());
        assertEquals(Y, a.getY());
        assertEquals(Z, a.getZ());
        assertEquals(w, a.getW());

        assertTrue(a.isPoint());
        assertFalse(a.isVector());
    }

    @Test
    void tupleWithWEqual0IsAVector() {
        double w = 0.0;

        Tuple a = new Tuple(X, Y, Z, w);

        assertEquals(X, a.getX());
        assertEquals(Y, a.getY());
        assertEquals(Z, a.getZ());
        assertEquals(w, a.getW());

        assertFalse(a.isPoint());
        assertTrue(a.isVector());
    }

    @Test
    void pointCreatesTuplesWithWEqual1() {
        Tuple expected = new Tuple(X, Y, Z, 1.0);
        Tuple actual = Tuple.point(X, Y, Z);

        assertEquals(expected, actual);
    }

    @Test
    void vectorCreatesTuplewithWEqual0() {
        Tuple expected = new Tuple(X, Y, Z, 0.0);
        Tuple actual = Tuple.vector(X, Y, Z);

        assertEquals(expected, actual);
    }

    @Test
    void addingTwoTuples() {
        Tuple a1 = Tuple.point(3, -2, 5);
        Tuple a2 = Tuple.vector(-2, 3, 1);

        Tuple expected = Tuple.point(1, 1, 6);
        Tuple actual = a1.add(a2);

        assertEquals(expected, actual);
    }

    @Test
    void subtractingTwoPoints() {
        Tuple p1 = Tuple.point(3, 2, 1);
        Tuple p2 = Tuple.point(5, 6, 7);

        Tuple expected = Tuple.vector(-2, -4, -6);
        Tuple actual = p1.subtract(p2);

        assertEquals(expected, actual);
    }

    @Test
    void subtractingAVectorFromAPoint() {
        Tuple p = Tuple.point(3, 2, 1);
        Tuple v = Tuple.vector(5, 6,7 );

        Tuple expected = Tuple.point(-2, -4, -6);
        Tuple actual = p.subtract(v);

        assertEquals(expected, actual);
    }

    @Test
    void subtractingTwoVectors() {
        Tuple v1 = Tuple.vector(3, 2, 1);
        Tuple v2 = Tuple.vector(5, 6, 7);

        Tuple expected = Tuple.vector(-2, -4, -6);
        Tuple actual = v1.subtract(v2);

        assertEquals(expected, actual);
    }
}