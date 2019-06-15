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
public class Matrix4x4 {
    protected double[][] matriz;

    public Matrix4x4() {
        this.matriz = new double[4][4];
    }
    
    public static Point3 times(Matrix4x4 matriz ,Point3 punto){
        return null;
    
    }
    
    public static Matrix4x4 times(Matrix4x4 m1 ,Matrix4x4 m2){
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
