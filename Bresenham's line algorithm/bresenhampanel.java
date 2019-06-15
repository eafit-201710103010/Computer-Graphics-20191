

/**
 * Clase donde se implementa la solucion al reto propuesto en la clase de 
 * Compuacion Grafica usando la solucion al algoritmo de Bresenham encontrada en el sitio web:
 * https://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Java
 * 
 * El esqueleto del programa, asi como los metodos plot() 
 * y drawLine() son tomados de la pagina mencionada
 * 
 * El metodo circulo() es de implementacion propia
 * 
 * @author Luisa Maria Vasquez Gomez
 * @version 31/01/2019
 * 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javafx.util.Pair;


class BresenhamPanel extends JPanel {

    private final int pixelSize = 1;

    BresenhamPanel() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //Numero de subdivisiones del circulo
        int n=4;
        circulo(g, n);
    }

    private void circulo(Graphics g, int n) {
        //Dibujo del circulo delimitador
        g.setColor(Color.black);
        int origenx = (getWidth() / 2) - 150;
        int origeny = (getHeight() / 2) - 150;
        g.drawOval(origenx, origeny, 300, 300);

        //x y y dibujados
        int xd, yd;
        int origenx1 = 0;
        int origeny1 = 0;
        Pair[] puntos = new Pair[n];
        
        //Ciclo que ingresa en un arreglo los puntos que subdividen al circulo
        for (int i = 0; i < n; i++) {
            //Se usa la formula x = r*cos(angulo) y y = r*sin(angulo)
            xd = origenx1 + (int)(150*Math.cos((2* Math.PI/n)*i));
            yd = origeny1 - (int)(150*Math.sin((2* Math.PI/n)*i));
            Pair punto = new Pair(xd, yd);
            puntos[i] = punto;
        }

        //Ciclo que recorre el arreglo y va dibujando las lineas segun sea el caso
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {    
                //Solo no se dibujan las lineas a los puntos adyacentes
                if (Math.abs(i - j) > 1 && Math.abs(i - j) < n-1) {                    
                    int d1 = (int) puntos[i].getKey();
                    int d2 = (int) puntos[i].getValue();
                    int d3 = (int) puntos[j].getKey();
                    int d4 = (int) puntos[j].getValue();
                    drawLine(g, d1, d2, d3, d4);   
                }               
            }
        }
    }

    private void plot(Graphics g, int x, int y) {
        int w = (getWidth() - 1) / pixelSize;
        int h = (getHeight() - 1) / pixelSize;
        int maxX = (w - 1) / 2;
        int maxY = (h - 1) / 2;

        int borderX = getWidth() - ((2 * maxX + 1) * pixelSize + 1);
        int borderY = getHeight() - ((2 * maxY + 1) * pixelSize + 1);
        int left = (x + maxX) * pixelSize + borderX / 2;
        int top = (y + maxY) * pixelSize + borderY / 2;

        g.setColor(Color.blue);
        g.drawOval(left, top, pixelSize, pixelSize);
    }

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        // delta of exact value and rounded value of the dependent variable
        int d = 0;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point

        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;

        int x = x1;
        int y = y1;

        if (dx >= dy) {
            while (true) {
                plot(g, x, y);
                if (x == x2) {
                    break;
                }
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                plot(g, x, y);
                if (y == y2) {
                    break;
                }
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
    }
}
