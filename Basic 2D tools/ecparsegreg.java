
package compgraf;
import java.awt.Point;
import java.util.Optional;
import javafx.util.Pair;
/**
 * Clase que represents la ecuacion parametrica de la recta
 * @version 13/02/2019
 * @author Luisa Maria Vasquez Gomez
 */
public class EcParSegReg {
    double a1;
    double a2;
    double v1;
    double v2;
    
    /**
     *  A partir de dos puntos genera los parametros de la ecuacion 
     *  que son un punto perteneciente a la recta y los componentes del vector
     */
    public EcParSegReg(double x1, double y1,double x2, double y2){
        v1= x2-x1;
        v2= y2-y1;
        a1=x1;
        a2=y1;
    }
    
    /**
     * Método que haya en que punto u o lamba se cruzan dos rectas a partir de sus ecuaciones 
     * parametricas.Implementado igualando los x y y de cada una y despejando.
     */
    public static Pair solve (EcParSegReg a, EcParSegReg b){
        double arr =(a.v2*b.a1)-(a.v2*a.a1)-(b.a2*a.v1)+(a.a2*a.v1);
        double den = ((b.v2*a.v1)-(a.v2*b.v1));
        double lam2 = arr/den;
        double lam1 =(b.a1 - a.a1 + (lam2*b.v1))/a.v1;
        Pair res = new Pair(lam1,lam2);       
     return res;
    }
   
    
}
