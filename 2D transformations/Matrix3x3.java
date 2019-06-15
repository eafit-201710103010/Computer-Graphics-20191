/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transformaciones;

/**
 *
 * @author lmvasquezg
 */
public class Matrix3x3 {

    protected double[][] matriz;

    public Matrix3x3() {
        this.matriz = new double[3][3];
    }

    public static Point2 times(Matrix3x3 m, Point2 p) {
        Point2 result = new Point2(0, 0, 0);
        result.x = (m.matriz[0][0] * p.x) + (m.matriz[0][1] * p.y) + (m.matriz[0][2] * p.w);
        result.y = (m.matriz[1][0] * p.x) + (m.matriz[1][1] * p.y) + (m.matriz[1][2] * p.w);
        result.w = (m.matriz[2][0] * p.x) + (m.matriz[2][1] * p.y) + (m.matriz[2][2] * p.w);
        return result;
    }

    public static Matrix3x3 times(Matrix3x3 m1, Matrix3x3 m2) {
        Matrix3x3 sol = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double r1 = m1.matriz[i][0] * m2.matriz[0][j];
                double r2 = m1.matriz[i][1] * m2.matriz[1][j];
                double r3 = m1.matriz[i][2] * m2.matriz[2][j];
                double val = r1 + r2 + r3;
                sol.matriz[i][j] = val;
            }
        }
        return sol;

    }

}
