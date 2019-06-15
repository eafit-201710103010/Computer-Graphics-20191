/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scene;

import Math.Point;
import Math.Vector4;
import Math.Ray;

/**
 *
 * @author htrefftz
 */
public class Shader {
    /**
     * Computes the color of a point "point", on a surface with normal "normal",
     * given the material properties of the object "material"
     * @param point 3D coordinates of the point
     * @param normal normal of the surface at point "point"
     * @param material material  of the object 
     * @return 
     */
    public static Colour computeColor(Point point, Vector4 normal, Material material) {
        normal.normalize();
        // We will add all the colors in acum
        Colour acum = new Colour(0, 0, 0);
        // Compute the Ambient Reflection
        Colour AmbientReflection = Colour.multiply(Colour.multiply(Scene.ambientLight.color, material.color), 
                material.Ka);
        acum = Colour.add(acum, AmbientReflection);
        // Compute the Diffuse Reflection, respect to all point lights
        for(PointLight pl: Scene.pointLights) {
            Vector4 light = new Vector4(point, pl.point);
            Ray shadowRay = new Ray(point, light);
            // Check if the object is in the shadow with respect to this source
            // of life. If it is, do not add diffuse reflection
            if(!Scene.intersectRayForShadow(shadowRay)) {
                light.normalize();
                // Acá se debe agregar el producto entre normal y light (***)
                double scalar = Vector4.dotProduct(light,normal)*material.Kd;
                // If dot product is < 0, the point is not receiving light from
                // this source.
                if(scalar < 0) scalar = 0;
                Colour DiffuseReflection = Colour.multiply(Colour.multiply(pl.color, material.color), 
                        scalar);
                acum = Colour.add(acum, DiffuseReflection);
                //Hallar vectores unitario R y V
                Vector4 R = Vector4.reflection(light, normal);
                Vector4 V = new Vector4( new Point(0, 0, 0),point);
                R.normalize();
                V.normalize();
                
                //Elevar al coeficiente especular (n) y multiplicar por Ks 
                double scalar2 = Math.pow(Vector4.dotProduct(R, V), material.n) * material.Ks;
                //Si es menos a cero, igualar a cero
                if(Vector4.dotProduct(R, V)<0.0) scalar2=0;
                //Obtengo el color de la luz
                Colour SpecularReflection = Colour.multiply(pl.color, scalar2);
                //Lo agrego al acumulado
                acum = Colour.add(acum, SpecularReflection);

            }
 
        }        
        return acum;
    }
}
