
/**
 * Clase donde se implementa la solucion al reto propuesto en la clase de 
 * Compuacion Grafica usando la solucion al algoritmo de Bresenham encontrada en el sitio web:
 * https://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Java
 * 
 * Todo el codigo de esta clase es de autoria de la pagina mencionada
 * @version 31/01/2019
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
 
public class Bresenham {
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Bresenham::run);
    }
 
    private static void run()  {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setTitle("Bresenham");
 
        f.getContentPane().add(new BresenhamPanel());
        f.pack();
 
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
 