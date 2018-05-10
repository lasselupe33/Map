package helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class VectorMathTest {
    private float[] vector1 = new float[] {1,1};
    private float[] vector2 = new float[] {0,1};
    private float[] vector3 = new float[] {1,0};
    private float[] vector4 = new float[] {0,0};
    private float[] vector5 = new float[] {-1,-1};
    private float[] vector6 = new float[] {0,-1};
    private float[] vector7 = new float[] {-1,0};
    private float[] vector8 = new float[] {-1,1};
    private float[] vector9 = new float[] {1,-1};

    // Actual vectors in the program
    private float[] vector10 = new float[] {(float)0.0005102, (float)0.000397};
    private float[] vector11 = new float[] {(float)-0.0001706, (float)0.000232};

    @Test
    public void scalarProduct() throws Exception {
        assertEquals(2, VectorMath.scalarProduct(vector1, vector1),0);
        assertEquals(1, VectorMath.scalarProduct(vector1, vector2),0);
        assertEquals(1, VectorMath.scalarProduct(vector1, vector3),0);
        assertEquals(0, VectorMath.scalarProduct(vector1, vector4),0);
        assertEquals(-2, VectorMath.scalarProduct(vector1, vector5),0);
        assertEquals(-1, VectorMath.scalarProduct(vector1, vector6),0);
        assertEquals(-1, VectorMath.scalarProduct(vector1, vector7),0);
        assertEquals(0, VectorMath.scalarProduct(vector1, vector8),0);
        assertEquals(0, VectorMath.scalarProduct(vector1, vector9),0);

        assertEquals(5.063881758360367E-9, VectorMath.scalarProduct(vector10, vector11),0);
    }

    @Test
    public void determinant() throws Exception {
        assertEquals(0, VectorMath.determinant(vector1, vector1),0);
        assertEquals(1, VectorMath.determinant(vector1, vector2),0);
        assertEquals(-1, VectorMath.determinant(vector1, vector3),0);
        assertEquals(0, VectorMath.determinant(vector1, vector4),0);
        assertEquals(0, VectorMath.determinant(vector1, vector5),0);
        assertEquals(-1, VectorMath.determinant(vector1, vector6),0);
        assertEquals(1, VectorMath.determinant(vector1, vector7),0);
        assertEquals(2, VectorMath.determinant(vector1, vector8),0);
        assertEquals(-2, VectorMath.determinant(vector1, vector9),0);

        assertEquals(1.8609460994412075E-7, VectorMath.determinant(vector10, vector11),0);
    }

    @Test
    public void angle() throws Exception {
        assertEquals(0, VectorMath.angle(vector1, vector1),0);
        assertEquals(45, VectorMath.angle(vector1, vector2),0);
        assertEquals(-45, VectorMath.angle(vector1, vector3),0);
        assertEquals(0, VectorMath.angle(vector1, vector4),0);
        assertEquals(180, VectorMath.angle(vector1, vector5),0);
        assertEquals(-135, VectorMath.angle(vector1, vector6),0);
        assertEquals(135, VectorMath.angle(vector1, vector7),0);
        assertEquals(90, VectorMath.angle(vector1, vector8),0);
        assertEquals(-90, VectorMath.angle(vector1, vector9),0);

        assertEquals(88.4412903048904, VectorMath.angle(vector10, vector11),0);
    }

}