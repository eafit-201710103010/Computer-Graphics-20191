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
public class Vector3 {
    double u;
    double v;
    double w;
    
    public Vector3(double u, double v, double w){
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public static Vector3 crossProduct(Vector3 v1, Vector3 v2) {       
        double x = (v1.v * v2.w) - (v1.w * v2.v);
        double y = (v1.w * v2.u) - (v1.u * v2.w);
        double z = (v1.u * v2.v) - (v1.v * v2.u);

        Vector3 vect = new Vector3(x,y,z);

        return vect;

    }

    public static double dotProduct(Vector3 v1, Vector3 v2) {
        double c1 = v1.u * v2.u;
        double c2 = v1.v * v2.v;
        double c3 = v1.w * v2.w;

        return (c1 + c2 + c3);
    }

    public double magnitude() {
        double u2 = Math.pow(u, 2);
        double v2 = Math.pow(v, 2);
        double w2 = Math.pow(w, 2);
        double sum = u2 + v2 + w2;
        return Math.sqrt(sum);
    }

    public Vector3 normalize() {
        double magnitud = magnitude();
        u /= magnitud;
        v /= magnitud;
        w /= magnitud;
        return new Vector3(u, v, w);
    }
    public String toString(){
        String a = u+", "+v+", "+w;
        return a;
    }
}