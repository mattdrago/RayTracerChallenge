package drago.rtc;

import java.util.Arrays;

public class Matrix {

    private final double[][] values;

    private static final double EPSILON= 0.00001;

    Matrix(double[][] values) {

        this.values = new double[values.length][];

        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i].clone();
        }

    }

    public static Matrix translation(double x, double y, double z) {
        Matrix translation = Matrix.identity(4);
        translation.values[0][3] = x;
        translation.values[1][3] = y;
        translation.values[2][3] = z;

        return translation;
    }

    public static Matrix scaling(double x, double y, double z) {
        Matrix scaling = Matrix.identity(4);
        scaling.values[0][0] = x;
        scaling.values[1][1] = y;
        scaling.values[2][2] = z;

        return scaling;
    }

    public static Matrix rotationX(double radians) {
        Matrix rotationX = identity(4);

        double cosR = Math.cos(radians);
        double sinR = Math.sin(radians);

        rotationX.values[1][1] = cosR;
        rotationX.values[2][2] = cosR;
        rotationX.values[2][1] = sinR;
        rotationX.values[1][2] = -sinR;

        return rotationX;
    }

    public static Matrix rotationY(double radians) {
        Matrix rotationY = identity(4);

        double cosR = Math.cos(radians);
        double sinR = Math.sin(radians);

        rotationY.values[0][0] = cosR;
        rotationY.values[2][2] = cosR;
        rotationY.values[2][0] = -sinR;
        rotationY.values[0][2] = sinR;

        return rotationY;
    }

    public static Matrix rotationZ(double radians) {
        Matrix rotationZ = identity(4);

        double cosR = Math.cos(radians);
        double sinR = Math.sin(radians);

        rotationZ.values[0][0] = cosR;
        rotationZ.values[1][1] = cosR;
        rotationZ.values[1][0] = sinR;
        rotationZ.values[0][1] = -sinR;

        return rotationZ;
    }

    public static Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        Matrix shearing = Matrix.identity(4);

        shearing.values[0][1] = xy;
        shearing.values[0][2] = xz;
        shearing.values[1][0] = yx;
        shearing.values[1][2] = yz;
        shearing.values[2][0] = zx;
        shearing.values[2][1] = zy;

        return shearing;
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

    public Tuple multiplyBy(Tuple operand) {
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

        for (int oldRowI = 0, newRowI = 0; oldRowI < oldRows; oldRowI++, newRowI++) {
            if(oldRowI == rowToRemove) {
                newRowI--;
                continue;
            }

            for (int oldColI = 0, newColI = 0; oldColI < oldCols; oldColI++, newColI++) {
                if(oldColI == columnToRemove) {
                    newColI--;
                    continue;
                }

                newValues[newRowI][newColI] = values[oldRowI][oldColI];
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
