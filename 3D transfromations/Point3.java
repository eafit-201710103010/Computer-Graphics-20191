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
public class Point3 {
    
    protected double x;
    protected double y;
    protected double z;
    protected double w;
    
    public Point3(double x, double y,double z, double w){
    this.x=x;
    this.y=y;
    this.z=z;
    this.w=w;
    }
    
    public void normalize(){
    x=x/w;
    y=y/w;
    z=z/w;
    w=1;
    }    
}
