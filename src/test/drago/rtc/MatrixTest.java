package drago.rtc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void constructingAndInspectingA4x4Matrix() {
        Matrix m = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {5.5, 6.5, 7.5, 8.5},
                {9, 10, 11, 12},
                {13.5, 14.5, 15.5, 16.5}
        });

        assertEquals(1, m.get(0, 0));
        assertEquals(4, m.get(0, 3));
        assertEquals(5.5, m.get(1,0));
        assertEquals(7.5, m.get(1,2));
        assertEquals(11, m.get(2,2));
        assertEquals(13.5, m.get(3,0));
        assertEquals(15.5, m.get(3,2));
    }

    @Test
    void constructedMatrixCannotBeManipulated() {
        double[][] values = {
                {1, 2, 3, 4},
                {5, 6, 7, 8}
        };

        Matrix m = new Matrix(values);

        values[0][0]++;

        assertEquals(1, m.get(0, 0));

    }

    @Test
    void a2x2MatrixCanBeRepresented() {
        Matrix m = new Matrix(new double[][] {
                {-3, 5},
                {1, -2}
        });

        assertEquals(-3, m.get(0,0));
        assertEquals(5, m.get(0,1));
        assertEquals(1, m.get(1,0));
        assertEquals(-2, m.get(1, 1));
    }

    @Test
    void a3x3MatrixCanBeRepresented() {
        Matrix m = new Matrix(new double[][] {
                {-3, 5, 0},
                {1, -2, -7},
                {0, 1, 1}
        });

        assertEquals(-3, m.get(0, 0));
        assertEquals(-2, m.get(1, 1));
        assertEquals(1, m.get(2, 2));
    }

    @Test
    void matrixEqualityWithIdenticalMatrices() {
        Matrix m1 = new Matrix(new double[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        });

        Matrix m2 = new Matrix(new double[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}

        });

        assertTrue(m1.equals(m2));
    }

    @Test
    void matrixEqualityWithDifferentMatrices() {
        Matrix m1 = new Matrix(new double[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        });

        Matrix m2 = new Matrix(new double[][] {
                {13, 14, 15, 16},
                {9, 10, 11, 12},
                {5, 6, 7, 8},
                {1, 2, 3, 4}
        });

        assertFalse(m1.equals(m2));
    }

    @Test
    void multiplyingTwoMatrices() {
        Matrix m1 = new Matrix(new double[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 8, 7, 6},
                {5, 4, 3, 2}
        });

        Matrix m2 = new Matrix(new double[][] {
            {-2, 1, 2, 3},
            {3, 2, 1, -1},
            {4, 3, 6, 5},
            {1, 2, 7, 8}
        });

        Matrix expected = new Matrix(new double[][] {
                {20, 22, 50, 48},
                {44, 54, 114, 108},
                {40, 58, 110, 102},
                {16, 26, 46, 42}
        });

        Matrix actual = m1.multiplyBy(m2);

        assertEquals(expected, actual);
    }

    @Test
    void matrixMultipliedByATuple() {
        Matrix m = new Matrix(new double[][] {
                {1, 2, 3, 4},
                {2, 4, 4, 2},
                {8, 6, 4, 1},
                {0, 0, 0, 1}
        });

        Tuple t = new Tuple(1, 2, 3, 1);

        Tuple expected = new Tuple(18, 24, 33, 1);
        Tuple actual = m.multiplyBy(t);

        assertEquals(expected, actual);
    }

    @Test
    void convertTupleToMatrix() {
        Tuple t = new Tuple(1, 2, 3, 4);

        Matrix expected = new Matrix(new double[][] {
                {1},
                {2},
                {3},
                {4}
        });

        Matrix actual = Matrix.toMatrix(t);

        assertEquals(expected, actual);
    }

    @Test
    void convertMatrixToTuple() {
        Matrix m = new Matrix(new double[][] {
                {1},
                {2},
                {3},
                {4}
        });

        Tuple expected = new Tuple(1, 2, 3, 4);
        Tuple actual = Matrix.fromMatrix(m);

        assertEquals(expected, actual);
    }
}