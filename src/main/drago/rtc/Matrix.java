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
}