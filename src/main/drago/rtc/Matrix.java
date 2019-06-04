package drago.rtc;

import java.util.Arrays;

class Matrix {

    private final double[][] values;

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
        return Arrays.deepEquals(values, matrix.values);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
    }

    Matrix multiplyBy(Matrix operand) {
        int itemCount = values.length;
        int rowCount = values.length;
        int colCount = operand.values[0].length;

        double[][] newValues = new double[rowCount][];

        for (int rowi = 0; rowi < rowCount; rowi++) {
            newValues[rowi] = new double[colCount];

            for (int coli = 0; coli < colCount; coli++) {
                for (int itemi = 0; itemi < itemCount; itemi++) {
                    newValues[rowi][coli] += values[rowi][itemi] * operand.values[itemi][coli];
                }
            }
        }

        return new Matrix(newValues);
    }

    Tuple multiplyBy(Tuple operand) {
        Matrix result = this.multiplyBy(toMatrix(operand));

        return fromMatrix(result);
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
}
