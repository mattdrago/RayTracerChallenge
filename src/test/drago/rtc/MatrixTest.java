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

    @Test
    void multiplyingAMatrixByTheIdentityMatrix() {
        Matrix m = new Matrix(new double[][] {
                {0, 1, 2, 4},
                {1, 2, 4, 8},
                {2, 4, 8, 16},
                {4, 8, 16, 32}
        });

        Matrix identity = Matrix.identity(4);

        Matrix result = m.multiplyBy(identity);

        assertEquals(m, result);
    }

    @Test
    void transposeAMatrix() {
        Matrix m = new Matrix(new double[][] {
                {0, 9, 3, 0},
                {9, 8, 0, 8},
                {1, 8, 5, 3},
                {0, 0, 5, 8}
        });

        Matrix expected = new Matrix(new double[][] {
                {0, 9, 1, 0},
                {9, 8, 8, 0},
                {3, 0, 5, 5},
                {0, 8, 3, 8}
        });

        Matrix actual = m.transpose();

        assertEquals(expected, actual);
    }

    @Test
    void transposingTheIdentityMatrix() {
        Matrix identity = Matrix.identity(4);

        Matrix result = identity.transpose();

        assertEquals(identity, result);
    }

    @Test
    void calculateTheDeterminantOfA2x2Matrix() {
        Matrix m = new Matrix(new double[][] {
                {1, 5},
                {-3, 2}
        });

        assertEquals(17, m.determinant());
    }

    @Test
    void aSubmatrixOfA3x3MatrixIsA2x2Matrix() {
        Matrix m = new Matrix(new double[][] {
                {1, 5, 0},
                {-3, 2, 7},
                {0, 6, -3}
        });

        Matrix expected = new Matrix(new double[][] {
                {-3, 2},
                {0, 6}
        });

        Matrix actual = m.submatrix(0, 2);

        assertEquals(expected, actual);
    }

    @Test
    void aSubMatrixOfA4x4MatrixIsA3x3Matrix() {
        Matrix m = new Matrix(new double[][] {
                {-6, 1, 1, 6},
                {-8, 5, 8, 6},
                {-1, 0, 8, 2},
                {-7, 1, -1, 1}
        });

        Matrix expected = new Matrix(new double[][] {
                {-6, 1, 6},
                {-8, 8, 6},
                {-7, -1, 1}
        });

        Matrix actual = m.submatrix(2, 1);

        assertEquals(expected, actual);
    }

    @Test
    void calculatingAMinorOfA3x3Matrix() {
        Matrix m = new Matrix(new double[][] {
                {3, 5, 0},
                {2, -1, -7},
                {6, -1, 5}
        });

        assertEquals(25, m.minor(1, 0));
    }

    @Test
    void calculatingACofactorOfA3x3Matrix() {
        Matrix m = new Matrix(new double[][] {
                {3, 5, 0},
                {2, -1, -7},
                {6, -1, 5}
        });

        assertEquals(-12, m.cofactor(0, 0));
        assertEquals(-25, m.cofactor(1, 0));
    }

    @Test
    void calculatingTheDeterminantOfA3x3Matrix() {
        Matrix m = new Matrix(new double[][] {
                {1, 2, 6},
                {-5, 8, -4},
                {2, 6, 4}
        });

        assertEquals(56, m.cofactor(0, 0));
        assertEquals(12, m.cofactor(0, 1));
        assertEquals(-46, m.cofactor(0, 2));
        assertEquals(-196, m.determinant());
    }

    @Test
    void calculatingTheDeterminantOfA4x4Matrix() {
        Matrix m = new Matrix(new double[][] {
                {-2, -8, 3, 5},
                {-3, 1, 7, 3},
                {1, 2, -9, 6},
                {-6, 7, 7, -9}
        });

        assertEquals(690, m.cofactor(0, 0));
        assertEquals(447, m.cofactor(0, 1));
        assertEquals(210, m.cofactor(0, 2));
        assertEquals(51, m.cofactor(0, 3));
        assertEquals(-4071, m.determinant());
    }

    @Test
    void testingAnInvertibleMatrixForInvertibility() {
        Matrix m = new Matrix(new double[][] {
                {6, 4, 4, 4},
                {5, 5, 7, 6},
                {4, -9, 3, -7},
                {9, 1, 7, -6}
        });

        assertTrue(m.isInvertible());
    }

    @Test
    void testingANoninvertibleMatrixForInvertibility() {
        Matrix m = new Matrix(new double[][] {
                {-4, 2, -2, -3},
                {9, 6, 2, 6},
                {0, -5, 1, -5},
                {0, 0, 0, 0}
        });

        assertFalse(m.isInvertible());
    }
    
    @Test
    void calculateTheInverseOfAMatrix() {
        Matrix m = new Matrix(new double[][] {
                {-5, 2, 6, -8},
                {1, -5, 1, 8},
                {7, 7, -6, -7},
                {1, -3, 7, 4}
        });
        
        Matrix expected = new Matrix(new double[][] {
                { 0.21805, 0.45113, 0.24060, -0.04511 },
                { -0.80827, -1.45677, -0.44361, 0.52068 },
                { -0.07895, -0.22368, -0.05263, 0.19737 },
                { -0.52256, -0.81391, -0.30075, 0.30639 }
        });

        Matrix actual = m.inverse();

        assertEquals(expected, actual);
    }
    
    @Test
    void calculateTheInverseOfASecondMatrix() {
        Matrix m = new Matrix(new double[][] {
                {8, -5, 9, 2},
                {7, 5, 6, 1},
                {-6, 0, 9, 6},
                {-3, 0, -9, -4}
        });
        
        Matrix expected = new Matrix(new double[][] { 
                {-0.15385, -0.15385, -0.28205, -0.53846},
                {-0.07692, 0.12308, 0.02564, 0.03077},
                {0.35897, 0.35897, 0.43590, 0.92308},
                {-0.69231, -0.69231, -0.76923, -1.92308}
        });
        
        Matrix actual = m.inverse();
        
        assertEquals(expected, actual);
    }
    
    @Test
    void calculateTheInverseOfAThirdMatrix() {
        Matrix m = new Matrix(new double[][] {
                {9, 3, 0, 9},
                {-5, -2, -6, -3},
                {-4, 9, 6, 4},
                {-7, 6, 6, 2}
        });
        
        Matrix expected = new Matrix(new double[][] {
                { -0.04074, -0.07778, 0.14444, -0.22222},
                { -0.07778, 0.03333, 0.36667, -0.33333},
                { -0.02901, -0.14630, -0.10926, 0.12963},
                { 0.17778, 0.06667, -0.26667, 0.33333}
        });

        Matrix actual = m.inverse();

        assertEquals(expected, actual);
    }

    @Test
    void multiplyingAProductByItsInverse() {
        Matrix a = new Matrix(new double[][] {
                {3, -9, 7, 3},
                {3, -8, 2, -9},
                {-4, 4, 4, 1},
                {-6, 5, -1, 1}
        });

        Matrix b = new Matrix(new double[][] {
                {8, 2, 2, 2},
                {3, -1, 7, 0},
                {7, 0, 5, 4},
                {6, -2, 0, 5}
        });

        Matrix c = a.multiplyBy(b);
        Matrix aNew = c.multiplyBy(b.inverse());

        assertEquals(a, aNew);

    }
}