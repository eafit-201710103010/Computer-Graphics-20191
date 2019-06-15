import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
 
/**
 * Clase principal del ejercicio propuesto en la semana 3 de computación gráfica
 * @author Luisa Maria Vasquez
 */
public class ClippingMain {
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClippingMain::run);
    }
 
    private static void run()  {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setTitle("Clipping");
 
        f.getContentPane().add(new Clipping());
        f.pack();
 
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
 