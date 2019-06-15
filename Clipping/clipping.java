
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import javafx.util.Pair;

/**
 * Implementacion del ejercicio propuesto en la clase de computacion grafica
 * para el algoritmo de Liang-Barsky
 * @version 06/02/2019
 * @author Luisa Maria Vasquez
 */
class Clipping extends JPanel implements MouseListener {

    // Coordinadas limites
    private int xMin = 100;
    private int xMax = 300;
    private int yMin = 100;
    private int yMax = 300;
    //Ya se tiene o no delimitador
    private boolean drawLimit = false;  
    // Lineas del grafico de conexion completa
    private final ArrayList<Line2D> lineas = new ArrayList<>();
    // Lineas que estan fuera del rango
    private final ArrayList<Line2D> nullLines = new ArrayList<>();
    // Rectangulo delimitador
    private Rectangle2D limite;
    // Clics hechos en el paanel
    private int iteraciones = 0;

    Clipping() {
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.WHITE);
        this.addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //Nùmero de subdivisiones del circulo
        int n = 12;
        
        // Método que genera las lineas del grafico de conexion completa
        circulo(g2d, n);

        // Cuando se tengan las coordinadas dibujar el cuadrado delimitador
        if (drawLimit) {            
            g.setColor(new Color(0, 0, 0));
            //Crear y dibujar el cuadrado limitador 
            Rectangle2D limited = new Rectangle2D.Double(xMin, yMin, xMax - xMin, yMax - yMin);
            g2d.draw(limited);
            limite = limited;
           
            // Una vez se tienen los limites se examina cada una de las lineas
            for (int i = 0; i < lineas.size(); i++) {
                //Obtengo linea original
                Line2D temp = lineas.get(i);
                // Creo el segmento con sus coordenadas
                LineSegment linea = new LineSegment((int) temp.getX1(), (int) temp.getY1(), (int) temp.getX2(), (int) temp.getY2());
                // Llamo al metodo que la corta 
                LineSegment clippedLine = clipLiangBarsky(linea);
                // Si la linea esta fuera dle rango la linea cortada sera nula
                if (clippedLine != null) {
                    //Creo y dibujo la linea que esta dentro del rango
                    Line2D lineDrawn = new Line2D.Double(clippedLine.x0, clippedLine.y0, clippedLine.x1, clippedLine.y1);
                    g.setColor(new Color(0, 255, 0));
                    g2d.draw(lineDrawn);
                    
                    // Busco la parte sobrante de la linea al principio y al final
                    int xSobIzq = (int) (lineDrawn.getX1() - temp.getX1());
                    int xSobDer = (int) (lineDrawn.getX2() - temp.getX2());
                    int ySobIzq = (int) (lineDrawn.getY1() - temp.getY1());
                    int ySobDer = (int) (lineDrawn.getY2() - temp.getY2());
                    g.setColor(new Color(255, 0, 0));
                    // Si el sobrante es más de 1 pixel dibujo el sobrante del inicio
                    if (Math.abs(xSobIzq) > 1 || Math.abs(ySobIzq) > 1) {
                        // Va desde el inicio de la recta original al inicio de la recta recortada
                        Line2D sobIn = new Line2D.Double(temp.getX1(), temp.getY1(), lineDrawn.getX1(), lineDrawn.getY1());
                        g2d.draw(sobIn);
                    }
                    // Si el sobrante es más de 1 pixel dibujo el sobrante del final
                    if (Math.abs(xSobDer) > 1 || Math.abs(ySobDer) > 1) {
                        // Va desde el fin de la recta recortada al fin de la recta original
                        Line2D sobDer = new Line2D.Double(temp.getX2(), temp.getY2(), lineDrawn.getX2(), lineDrawn.getY2());
                        g2d.draw(sobDer);
                    }
                } else {
                    // Agrego la linea al conjunto de lineas fuera del rango
                    nullLines.add(temp);
                }
            }
            // Dibujo las lineas fuera del rango
            for (int i = 0; i < nullLines.size(); i++) {
                g2d.setColor(new Color(255, 0, 0));
                g2d.draw(nullLines.get(i));
            }
        }

    }

    /**
     * Método que genera las lineas del graficod e cocnexion completa y las ingresa
     * en un ArrayList para luego ser clasificadas como dentro o fuera del circulo 
     * @param g Libreria Graphics
     * @param n Subdivisiones
     */
    private void circulo(Graphics2D g, int n) {
        //Dibujo del circulo delimitador
        g.setColor(Color.pink);
        int origenx = (getWidth() / 2) - 150;
        int origeny = (getHeight() / 2) - 150;
        g.drawOval(origenx, origeny, 300, 300);

        //x y y dibujados
        int xd, yd;
        int origenx1 = 250;
        int origeny1 = 250;
        Pair[] puntos = new Pair[n];

        //Ciclo que ingresa en un arreglo los puntos que subdividen al circulo
        for (int i = 0; i < n; i++) {
            //Se usa la formula x = r*cos(angulo) y y = r*sin(angulo)
            xd = origenx1 + (int) (150 * Math.cos((2 * Math.PI / n) * i));
            yd = origeny1 - (int) (150 * Math.sin((2 * Math.PI / n) * i));
            Pair punto = new Pair(xd, yd);
            puntos[i] = punto;
        }

        //Ciclo que recorre el arreglo y va creando las lineas del grafico
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int d1 = (int) puntos[i].getKey();
                int d2 = (int) puntos[i].getValue();
                int d3 = (int) puntos[j].getKey();
                int d4 = (int) puntos[j].getValue();
                Line2D line = new Line2D.Double(d1, d2, d3, d4);
                g.setColor(new Color(126, 26, 162));
                //g.draw(line);
                lineas.add(line);

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Cuento dos clics para saber coordenadas del cuadrado delimitador
        if (iteraciones == 0) {
            iteraciones++;
            xMin = e.getX();
            yMin = e.getY();
        } else {
            xMax = e.getX();
            yMax = e.getY();
            drawLimit = true;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * A partir de este punto se toma como referencia la implementación del algoritmo
     * de Liang-Barsky encontrada en:
     * https://github.com/donkike/Computer-Graphics/blob/master/LineClipping/LineClippingPanel.java
     * 
     * Este codigo no es de mi autoria, solo lo uso para realizar el ejercicio propuesto
     */
    private class LineSegment {

        public int x0;
        public int y0;
        public int x1;
        public int y1;

        public LineSegment(int x0, int y0, int x1, int y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
        }
    }

    public LineSegment clipLiangBarsky(LineSegment line) {
        //System.out.println("\nExecuting Liang-Barsky...");
        double u1 = 0, u2 = 1;
        int x0 = line.x0, y0 = line.y0, x1 = line.x1, y1 = line.y1;
        int dx = x1 - x0, dy = y1 - y0;
        int p[] = {-dx, dx, -dy, dy};
        int q[] = {x0 - xMin, xMax - x0, y0 - yMin, yMax - y0};
        for (int i = 0; i < 4; i++) {
            if (p[i] == 0) {
                if (q[i] < 0) {
                    return null;
                }
            } else {
                double u = (double) q[i] / p[i];
                if (p[i] < 0) {
                    u1 = Math.max(u, u1);
                } else {
                    u2 = Math.min(u, u2);
                }
            }
        }
        //System.out.println("u1: " + u1 + ", u2: " + u2);
        if (u1 > u2) {
            return null;
        }
        int nx0, ny0, nx1, ny1;
        nx0 = (int) (x0 + u1 * dx);
        ny0 = (int) (y0 + u1 * dy);
        nx1 = (int) (x0 + u2 * dx);
        ny1 = (int) (y0 + u2 * dy);
        return new LineSegment(nx0, ny0, nx1, ny1);
    }
}
