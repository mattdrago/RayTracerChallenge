package drago.rtc.foundations;

import drago.rtc.foundations.Tuple;
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
    void vectorCreatesTupleWithWEqual0() {
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

    @Test
    void subtractingAVectorFromTheZeroVector() {
        Tuple zero = Tuple.vector(0, 0, 0);
        Tuple v = Tuple.vector(1, -2, 3);

        Tuple expected = Tuple.vector(-1, 2, -3);
        Tuple actual = zero.subtract(v);

        assertEquals(expected, actual);
    }

    @Test
    void negatingATuple() {
        Tuple a = new Tuple(1, -2, 3, -4);

        Tuple expected = new Tuple(-1, 2, -3, 4);
        Tuple actual = a.negate();

        assertEquals(expected, actual);
    }

    @Test
    void multiplyingTupleByAScalar() {
        Tuple a = new Tuple(1, -2, 3, -4);

        Tuple expected = new Tuple(3.5, -7, 10.5, -14);
        Tuple actual = a.scale(3.5);

        assertEquals(expected, actual);
    }

    @Test
    void multiplyingTupleByAFraction() {
        Tuple a = new Tuple(1, -2, 3, -4);

        Tuple expected = new Tuple(0.5, -1, 1.5, -2);
        Tuple actual = a.scale(0.5);

        assertEquals(expected, actual);
    }

    @Test
    void dividingTupleByAScalar() {
        Tuple a = new Tuple(1, -2, 3, -4);

        Tuple expected = new Tuple(0.5, -1, 1.5, -2);
        Tuple actual = a.divide(2);

        assertEquals(expected, actual);
    }

    @Test
    void computeMagnitudeOfVector100() {
        Tuple v = Tuple.vector(1, 0, 0);

        assertEquals(1, v.magnitude());
    }

    @Test
    void computeMagnitudeOfVector010() {
        Tuple v = Tuple.vector(0, 1, 0);

        assertEquals(1, v.magnitude());
    }

    @Test
    void computeMagnitudeOfVector001() {
        Tuple v = Tuple.vector(0, 0, 1);

        assertEquals(1, v.magnitude());
    }


    @Test
    void computeMagnitudeOfVector123() {
        Tuple v = Tuple.vector(1, 2, 3);

        assertEquals(Math.sqrt(14), v.magnitude());
    }

    @Test
    void computeMagnitudeOfVectorNeg123() {
        Tuple v = Tuple.vector(-1, -2, -3);

        assertEquals(Math.sqrt(14), v.magnitude());
    }

    @Test
    void normaliseVector400() {
        Tuple v = Tuple.vector(4, 0, 0);

        Tuple expected = Tuple.vector(1, 0, 0);
        Tuple actual = v.normalise();

        assertEquals(expected, actual);
    }

    @Test
    void normaliseVector123() {
        Tuple v = Tuple.vector(1, 2,3 );

        Tuple expected = Tuple.vector(1/Math.sqrt(14), 2/Math.sqrt(14), 3/Math.sqrt(14));
        Tuple actual = v.normalise();

        assertEquals(expected, actual);
    }

    @Test
    void magnitudeOfNormalisedVectorIs1() {
        Tuple v = Tuple.vector(1, 2,3 );

        Tuple vNorm = v.normalise();

        assertEquals(1, vNorm.magnitude());
    }

    @Test
    void dotProductOfTwoTuples() {
        Tuple v1 = Tuple.vector(1, 2, 3);
        Tuple v2 = Tuple.vector(2, 3, 4);

        assertEquals(20, v1.dot(v2));
    }

    @Test
    void crossProductOfTwoVectors() {
        Tuple v1 = Tuple.vector(1, 2, 3);
        Tuple v2 = Tuple.vector(2, 3, 4);

        assertEquals(Tuple.vector(-1, 2, -1), v1.cross(v2));
        assertEquals(Tuple.vector(1, -2, 1), v2.cross(v1));
    }

    @Test
    void reflectingAVectorApproachingAt45Degrees() {
        Tuple v = Tuple.vector(1, -1, 0);
        Tuple normal = Tuple.vector(0, 1, 0);

        Tuple expectedReflection = Tuple.vector(1, 1, 0);
        Tuple actualReflection = v.reflect(normal);

        assertEquals(expectedReflection, actualReflection);
    }

    @Test
    void reflectingAVectorOffASlantedSurface() {
        Tuple v = Tuple.vector(0, -1, 0);
        Tuple normal = Tuple.vector(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0);

        Tuple expectedReflection = Tuple.vector(1, 0, 0);
        Tuple actualReflection = v.reflect(normal);

        assertEquals(expectedReflection, actualReflection);
    }

}