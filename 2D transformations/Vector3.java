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
public class Vector3 {

    double[] vector = new double[3];
    
    public Vector3(double x, double y, double z){
    vector[0]=x;
    vector[1]=y;
    vector[2]=z;
    
    }

    public static Vector3 crossProduct(Vector3 a, Vector3 b) {       
        double x = (a.vector[1]*b.vector[2])-(a.vector[2]*b.vector[1]);
        double y = (a.vector[2]*b.vector[0])-(a.vector[0]*b.vector[2]);
        double z = (a.vector[0]*b.vector[1])-(a.vector[1]*b.vector[0]);
        Vector3 r = new Vector3(x,y,z);
        return r;

    }

    public static double dotProduct(Vector3 a, Vector3 b) {
        double c1 = a.vector[0]*b.vector[0];
        double c2 = a.vector[1]*b.vector[1];
        double c3 = a.vector[2]*b.vector[2];
        return (c1+c2+c3);
    }

    public double Magnitude() {
        double x2 = Math.pow(vector[0],2);
        double y2 = Math.pow(vector[1],2);
        double z2 = Math.pow(vector[2],2);
        double sum = x2+y2+z2;
        return Math.sqrt(sum);
    }

    public void Normalize() {
        double mag = Magnitude();
        vector[0]=vector[0]/mag;
        vector[1]=vector[1]/mag;
        vector[2]=vector[2]/mag;

    }
    public String toString(){
        String a = vector[0]+" "+vector[1]+" "+vector[2];
        return a ;
    }

    
}
