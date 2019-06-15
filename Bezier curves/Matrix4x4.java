/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transformaciones3D;

/**
 *
 * @author lmvasquezg
 */
public class Matrix4x4 {
    protected double[][] matriz;

    public Matrix4x4() {
        this.matriz = new double[4][4];
    }
    

    public static Point3 times(Matrix4x4 m, Point3 p) {

        Point3 result = new Point3(0.0, 0.0, 0.0, 0.0);
        result.x = (m.matriz[0][0] * p.x) + (m.matriz[0][1] * p.y) + (m.matriz[0][2] * p.z) + (m.matriz[0][3] * p.w);
        result.y = (m.matriz[1][0] * p.x) + (m.matriz[1][1] * p.y) + (m.matriz[1][2] * p.z) + (m.matriz[1][3] * p.w);
        result.z = (m.matriz[2][0] * p.x) + (m.matriz[2][1] * p.y) + (m.matriz[2][2] * p.z) + (m.matriz[2][3] * p.w);
        result.w = (m.matriz[3][0] * p.x) + (m.matriz[3][1] * p.y) + (m.matriz[3][2] * p.z) + (m.matriz[3][3] * p.w);
        return result;
    }

    public static Matrix4x4 times(Matrix4x4 m1, Matrix4x4 m2) {
        Matrix4x4 sol = new Matrix4x4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double r1 = m1.matriz[i][0] * m2.matriz[0][j];
                double r2 = m1.matriz[i][1] * m2.matriz[1][j];
                double r3 = m1.matriz[i][2] * m2.matriz[2][j];
                double r4 = m1.matriz[i][3] * m2.matriz[3][j];
                double val = r1 + r2 + r3+r4;
                sol.matriz[i][j] = val;
            }
        }
        return sol;    
    }
    
    public void print2D() {
        double[][] mat = matriz;
        for (int i = 0; i < mat.length; i++){
            for (int j = 0; j < mat[i].length; j++){
                System.out.print(mat[i][j] + " ");
            }
            System.out.println("");
        }
    }

}
