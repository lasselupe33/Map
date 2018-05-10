package helpers;

public class VectorMath {
    /**
     * Compute the scalar product of two vectors
     * @param p first vector
     * @param q second vector
     * @return scalar product of vector p and vector q
     */
    public static double scalarProduct(float[] p, float[] q) {
        double product = 0;
        for (int i = 0; i < p.length; i++) {
            product += p[i] * q[i];
        }
        return product;
    }

    /**
     * Compute the determinant of two vectors
     * @param p first vector
     * @param q second vector
     * @return determinant of vector p and vector q
     */
    public static double determinant(float[] p, float[] q) {
        return p[0]*q[1] - p[1]*q[0];
    }

    /**
     * Compute the angle of two vectors
     * @param p first vector
     * @param q second vector
     * @return angle between -180 and 180
     */
    public static double angle(float[] p, float[] q) {
        return Math.toDegrees(Math.atan2(determinant(p, q), scalarProduct(p ,q)));
    }
}
