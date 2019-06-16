package drago.rtc.foundations;

import drago.rtc.foundations.Matrix;
import drago.rtc.foundations.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatrixTransformationTest {

    @Test
    void multiplyingByATranslationMatrix() {
        Matrix transform = Matrix.translation(5, -3, 2);
        Tuple point = Tuple.point(-3, 4, 5);

        Tuple expected = Tuple.point(2, 1, 7);
        Tuple actual = transform.multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void multiplyingByTheInverseOfATranslationMatrix() {
        Matrix transform = Matrix.translation(5, -3, 2);
        Tuple point = Tuple.point(-3, 4, 5);

        Tuple expected = Tuple.point(-8, 7, 3);
        Tuple actual = transform.inverse().multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void translationDoesNotAffectVectors() {
        Matrix transform = Matrix.translation(5, -3, 2);
        Tuple vector = Tuple.vector(-3, 4, 5);

        Tuple result = transform.multiplyBy(vector);

        assertEquals(vector, result);
    }

    @Test
    void aScalingMatrixAppliedToAPoint() {
        Matrix transform = Matrix.scaling(2, 3, 4);
        Tuple point = Tuple.point(-4, 6, 8);

        Tuple expected = Tuple.point(-8, 18, 32);
        Tuple actual = transform.multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void aScalingMatrixAppliedToAVector() {
        Matrix transform = Matrix.scaling(2, 3, 4);
        Tuple vector = Tuple.vector(-4, 6, 8);

        Tuple expected = Tuple.vector(-8, 18, 32);
        Tuple actual = transform.multiplyBy(vector);

        assertEquals(expected, actual);
    }

    @Test
    void multiplyingByTheInverseOfAScalingMatrix() {
        Matrix transform = Matrix.scaling(2, 3, 4);
        Tuple vector = Tuple.vector(-4, 6, 8);

        Tuple expected = Tuple.vector(-2, 2, 2);
        Tuple actual = transform.inverse().multiplyBy(vector);

        assertEquals(expected, actual);
    }

    @Test
    void reflectionIsScalingByANegativeValue() {
        Matrix transform = Matrix.scaling(-1, 1, 1);
        Tuple point = Tuple.point(2, 3, 4);

        Tuple expected = Tuple.point(-2, 3, 4);
        Tuple actual = transform.multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void rotatingAPointAroundTheXAxis() {
        Tuple point = Tuple.point(0, 1, 0);

        Tuple halfQuarterExpected = Tuple.point(0, Math.sqrt(2)/2, Math.sqrt(2)/2);
        Tuple fullQuarterExpected = Tuple.point(0, 0, 1);

        assertEquals(halfQuarterExpected, Matrix.rotationX(Math.PI / 4).multiplyBy(point));
        assertEquals(fullQuarterExpected, Matrix.rotationX(Math.PI / 2).multiplyBy(point));
    }

    @Test
    void TheInverseOfAnXRotationRotatesInTheOppositeDirection() {
        Tuple point = Tuple.point(0, 1, 0);

        Tuple expected = Tuple.point(0, Math.sqrt(2) / 2, - Math.sqrt(2)/2);
        Tuple actual = Matrix.rotationX(Math.PI / 4).inverse().multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void rotatingAPointAroundTheYAxis() {
        Tuple point = Tuple.point(0, 0, 1);

        Tuple halfQuarterExpected = Tuple.point(Math.sqrt(2)/2, 0, Math.sqrt(2)/2);
        Tuple fullQuarterExpected = Tuple.point(1, 0, 0);

        assertEquals(halfQuarterExpected, Matrix.rotationY(Math.PI / 4).multiplyBy(point));
        assertEquals(fullQuarterExpected, Matrix.rotationY(Math.PI / 2).multiplyBy(point));
    }

    @Test
    void rotatingAPointAroundTheZAxis() {
        Tuple point = Tuple.point(0, 1, 0);

        Tuple halfQuarterExpected = Tuple.point(- Math.sqrt(2)/2, Math.sqrt(2)/2, 0);
        Tuple fullQuarterExpected = Tuple.point(-1, 0, 0);

        assertEquals(halfQuarterExpected, Matrix.rotationZ(Math.PI / 4).multiplyBy(point));
        assertEquals(fullQuarterExpected, Matrix.rotationZ(Math.PI / 2).multiplyBy(point));
    }

    @Test
    void aShearingTransformationMovesXInProportionToY() {
        Tuple point = Tuple.point(2, 3, 4);

        Tuple expected = Tuple.point(5, 3, 4);
        Tuple actual = Matrix.shearing(1, 0, 0, 0, 0, 0).multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void aShearingTransformationMovesXInProportionToz() {
        Tuple point = Tuple.point(2, 3, 4);

        Tuple expected = Tuple.point(6, 3, 4);
        Tuple actual = Matrix.shearing(0, 1, 0, 0, 0, 0).multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void aShearingTransformationMovesYInProportionToX() {
        Tuple point = Tuple.point(2, 3, 4);

        Tuple expected = Tuple.point(2, 5, 4);
        Tuple actual = Matrix.shearing(0, 0, 1, 0, 0, 0).multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void aShearingTransformationMovesYInProportionToZ() {
        Tuple point = Tuple.point(2, 3, 4);

        Tuple expected = Tuple.point(2, 7, 4);
        Tuple actual = Matrix.shearing(0, 0, 0, 1, 0, 0).multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void aShearingTransformationMovesZInProportionToX() {
        Tuple point = Tuple.point(2, 3, 4);

        Tuple expected = Tuple.point(2, 3, 6);
        Tuple actual = Matrix.shearing(0, 0, 0, 0, 1, 0).multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void aShearingTransformationMovesZInProportionToY() {
        Tuple point = Tuple.point(2, 3, 4);

        Tuple expected = Tuple.point(2, 3, 7);
        Tuple actual = Matrix.shearing(0, 0, 0, 0, 0, 1).multiplyBy(point);

        assertEquals(expected, actual);
    }

    @Test
    void individualTransformationAreAppliedInSequence() {
        Tuple point = Tuple.point(1, 0, 1);

        Matrix rotationX = Matrix.rotationX(Math.PI / 2);
        Matrix scaling = Matrix.scaling(5, 5, 5);
        Matrix translation = Matrix.translation(10, 5, 7);

        // Apply rotation first
        Tuple point2 = rotationX.multiplyBy(point);
        assertEquals(Tuple.point(1, -1, 0), point2);

        // then apply scaling
        Tuple point3 = scaling.multiplyBy(point2);
        assertEquals(Tuple.point(5, -5, 0), point3);

        // then apply translation
        Tuple point4 = translation.multiplyBy(point3);
        assertEquals(Tuple.point(15, 0, 7), point4);
    }

    @Test
    void chainedTransformationsMustBeAppliedInReverseOrder() {
        Tuple point = Tuple.point(1, 0, 1);

        Matrix rotationX = Matrix.rotationX(Math.PI / 2);
        Matrix scaling = Matrix.scaling(5, 5, 5);
        Matrix translation = Matrix.translation(10, 5, 7);

        Matrix transform = translation.multiplyBy(scaling).multiplyBy(rotationX);

        Tuple result = transform.multiplyBy(point);

        assertEquals(Tuple.point(15, 0, 7), result);
    }
    
    @Test
    void theTransformationMatrixForTheDefaultOrientation() {
        Tuple from = Tuple.point(0, 0, 0);
        Tuple to = Tuple.point(0, 0, -1);
        Tuple up = Tuple.vector(0, 1, 0);
        
        Matrix result = Matrix.viewTransform(from, to, up);
        
        assertEquals(Matrix.identity(4), result);
    }
    
    @Test
    void aViewTransformationMatrixLookingInPositiveZDirection() {
        Tuple from = Tuple.point(0, 0, 0);
        Tuple to = Tuple.point(0, 0, 1);
        Tuple up = Tuple.vector(0, 1, 0);
        
        Matrix result = Matrix.viewTransform(from, to , up);
        
        assertEquals(Matrix.scaling(-1, 1, -1), result);
    }
    
    @Test
    void theViewTransformationMovesTheWorld() {
        Tuple from = Tuple.point(0, 0, 8);
        Tuple to = Tuple.point(0, 0, 0);
        Tuple up = Tuple.vector(0, 1, 0);
        
        Matrix result = Matrix.viewTransform(from, to , up);
        
        assertEquals(Matrix.translation(0, 0, -8), result);
    }
    
    @Test
    void anArbitraryViewTransformation() {
        Tuple from = Tuple.point(1, 3, 2);
        Tuple to = Tuple.point(4, -2, 8);
        Tuple up = Tuple.vector(1, 1, 0);
        
        Matrix expected = new Matrix(new double[][] {
                {-0.50709, 0.50709, 0.67612, -2.36643},
                {0.76772, 0.60609, 0.12122, -2.82843},
                {-0.35857, 0.59761, -0.71714, 0.00000},
                {0.00000, 0.00000, 0.00000, 1.00000}
        });

        Matrix actual = Matrix.viewTransform(from, to , up);

        assertEquals(expected, actual);
    }
}
