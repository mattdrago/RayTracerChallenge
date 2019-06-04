package drago.rtc;

import java.util.Arrays;

class Matrix {

    private final double[][] values;

    private static final double EPSILON= 0.00001;

    Matrix(double[][] values) {

        this.values = new double[values.length][];

        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i].clone();
        }

    }

    double get(int row, int column) {
        return values[row][column];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;

        if (values.length != matrix.values.length) {
            return false;
        } else {
            for (int rowI = 0; rowI < values.length; rowI++) {
                if (values[rowI].length != matrix.values[rowI].length) {
                    return false;
                } else {
                    for (int colI = 0; colI < values[rowI].length; colI++) {
                        if (!equalDoubles(values[rowI][colI], matrix.values[rowI][colI])) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean equalDoubles(double d1, double d2) {
        return Math.abs(d1 - d2) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
    }

    Matrix multiplyBy(Matrix operand) {
        int itemCount = values.length;
        int rowCount = values.length;
        int colCount = operand.values[0].length;

        double[][] newValues = new double[rowCount][colCount];

        for (int rowI = 0; rowI < rowCount; rowI++) {
            for (int colI = 0; colI < colCount; colI++) {
                for (int itemI = 0; itemI < itemCount; itemI++) {
                    newValues[rowI][colI] += values[rowI][itemI] * operand.values[itemI][colI];
                }
            }
        }

        return new Matrix(newValues);
    }

    Tuple multiplyBy(Tuple operand) {
        Matrix result = this.multiplyBy(toMatrix(operand));

        return fromMatrix(result);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(values);
    }

    static Matrix toMatrix(Tuple t) {
        return new Matrix(new double[][] {
                {t.getX()},
                {t.getY()},
                {t.getZ()},
                {t.getW()}
        });
    }

    static Tuple fromMatrix(Matrix m) {
        return new Tuple(m.values[0][0], m.values[1][0], m.values[2][0], m.values[3][0]);
    }

    static Matrix identity(int size) {
        double[][] values = new double[size][size];

        for (int i = 0; i < size; i++) {
            values[i][i] = 1;
        }

        return new Matrix(values);
    }

    Matrix transpose() {
        int rows = values.length;
        int cols = values[0].length;

        double[][] transposeValues = new double[cols][rows];

        for (int rowI = 0; rowI < rows; rowI++) {
            for (int colI = 0; colI < cols; colI++) {
                transposeValues[colI][rowI] = values[rowI][colI];
            }
        }

        return new Matrix(transposeValues);
    }

    double determinant() {
        double determinant = 0;
        int matrixSize = values.length;

        if(matrixSize == 2) {
            determinant = values[0][0] * values[1][1] - values[0][1] * values [1][0];
        } else {
            for (int colI = 0; colI < matrixSize; colI++) {
                determinant += values[0][colI] * cofactor(0, colI);
            }
        }

        return determinant;
    }

    public Matrix submatrix(int rowToRemove, int columnToRemove) {
        int oldRows = values.length;
        int oldCols = values[0].length;

        double[][] newValues = new double[oldRows - 1][oldCols - 1];

        for (int oldrowI = 0, newrowI = 0; oldrowI < oldRows; oldrowI++, newrowI++) {
            if(oldrowI == rowToRemove) {
                newrowI--;
                continue;
            }

            for (int oldcolI = 0, newcolI = 0; oldcolI < oldCols; oldcolI++, newcolI++) {
                if(oldcolI == columnToRemove) {
                    newcolI--;
                    continue;
                }

                newValues[newrowI][newcolI] = values[oldrowI][oldcolI];
            }
        }

        return new Matrix(newValues);
    }

    double minor(int row, int col) {
        return submatrix(row, col).determinant();
    }

    double cofactor(int row, int col) {
        return minor(row, col) * ((row + col) % 2 == 0 ? 1 : -1);
    }

    boolean isInvertible() {
        return determinant() != 0;
    }

    Matrix inverse() {
        if(!isInvertible()) {
            return null;
        }

        double determinant = determinant();
        double[][] newValues = new double[values.length][values[0].length];

        for (int rowI = 0; rowI < values.length; rowI++) {
            for (int colI = 0; colI < values[0].length; colI++) {
                newValues[colI][rowI] = cofactor(rowI, colI) / determinant;
            }
        }

        return new Matrix(newValues);
    }
}
