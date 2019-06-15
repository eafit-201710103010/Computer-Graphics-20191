
package compgraf;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/*
 * Clase principal de donde se llama las funciones implementadas 
 * para dar solucion al ejercicio propuesto de la semana 4 en computacion grafica
 * @version 13/02/2019
 * @author Luisa Maria Vasquez
 */
public class Casita {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Casita::run);
    }

    private static void run() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setTitle("Clipping");
        f.getContentPane().add(new CasitaDib());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

    }

}
