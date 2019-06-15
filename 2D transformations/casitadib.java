package transformaciones;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Implementacion del ejercicio propuesto en la clase de computacion grafica
 * para realizar multiples transformaciones a un dibujo
 *
 * @version 20/02/2019
 * @author Luisa Maria Vasquez
 */
class CasitaDib extends JPanel
        implements KeyListener {

    //Dirección archivo txt
    private static final String FILENAME = "C:\\Users\\luisa\\Documents\\Quinto semestre\\Computación gráfica\\Reto transformaciones\\Transformaciones\\src\\transformaciones\\casita.txt";
    // ArrayList de los puntos inciiales d ela figura
    private static final ArrayList<Point> puntos = new ArrayList<>();
    //ArrayList de objetos Line2D de las lineas dibujadas 
    private static final ArrayList<Line2D> lineas = new ArrayList<>();
    //ArrayList de tipo Edge que representan als lineas dibujadas
    private static final ArrayList<Edge> edgesCasita = new ArrayList<>();

    //Definir que transofrmacione estoy realizando
    boolean translacion = false;
    boolean rotacion = false;
    boolean escalamiento = false;
    //Direccion de la transformación
    int direccion = 0;
    //Solo leer el archivo .txt una vez
    boolean leido = false;

    public CasitaDib() {
        setBackground(Color.WHITE);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.removeAll();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        // Determinar origen del plano
        int origeny = getHeight() / 2;
        int origenx = (getWidth() / 2);

        // Dibujar los ejes
        Line2D ejey = new Line2D.Double(origenx, 0, origenx, getHeight());
        Line2D ejex = new Line2D.Double(0, origeny, getWidth(), origeny);
        g2d.draw(ejex);
        g2d.draw(ejey);

        //La casita se dibujara en morado
        g2d.setColor(new Color(172, 4, 172));

        //Leer el archivo de texto
        if (!leido) {
            leerArchivo(g2d);
        }
        // Seleccionar que transformacion voy a hacer
        if (translacion) {
            transladar(g2d, direccion);
        } else if (rotacion) {
            rotar(g2d, direccion);
        } else if (escalamiento) {
            escalar(g2d, direccion);
        }

    }

    public void leerArchivo(Graphics2D g2d) {

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String sCurrentLine;
            //Leer cantidad de puntos
            int cpuntos;
            sCurrentLine = br.readLine();
            cpuntos = Integer.parseInt(sCurrentLine);
            //Leer puntos y agregarlos al arreglo
            for (int i = 0; i < cpuntos; i++) {
                sCurrentLine = br.readLine();
                String[] xy = sCurrentLine.split(" ");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                Point punto = new Point(x, y);
                puntos.add(punto);
            }
            //Leer cantidad de lineas 
            int clineas;
            sCurrentLine = br.readLine();
            clineas = Integer.parseInt(sCurrentLine);

            // Determinar origen del plano
            int origeny = getHeight() / 2;
            int origenx = (getWidth() / 2);

            //Leer y dibujar  las lineas
            for (int i = 0; i < clineas; i++) {
                sCurrentLine = br.readLine();
                String[] edges = sCurrentLine.split(" ");

                // Obtener coordenadas de inicio y fin adaptandolas al plano
                double x1 = puntos.get(Integer.parseInt(edges[0])).x + origenx;
                double y1 = origeny - puntos.get(Integer.parseInt(edges[0])).y;
                double x2 = puntos.get(Integer.parseInt(edges[1])).x + origenx;
                double y2 = origeny - puntos.get(Integer.parseInt(edges[1])).y;

                // Crear la linea en terminos de las clases existentes
                Point a = new Point(puntos.get(Integer.parseInt(edges[0])).x, puntos.get(Integer.parseInt(edges[0])).y);
                Point b = new Point(puntos.get(Integer.parseInt(edges[1])).x, puntos.get(Integer.parseInt(edges[1])).y);
                Edge edge = new Edge(a, b);
                edgesCasita.add(edge);
                // Dibujar la linea y agregarlo al arreglo
                Line2D linea = new Line2D.Double(x1, y1, x2, y2);
                lineas.add(linea);
                g2d.draw(linea);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        leido = true;
    }

    public void transladar(Graphics2D g2d, int dir) {
        //Crear matriz de transformación
        Matrix3x3 trans = new Matrix3x3();
        trans.matriz[0][0] = 1;
        trans.matriz[1][1] = 1;
        trans.matriz[2][2] = 1;

        switch (dir) {
            case 1:
                // Derecha
                trans.matriz[0][2] = 10;
                trans.matriz[1][2] = 0;
                break;
            case 2:
                // Izquierda 
                trans.matriz[0][2] = -10;
                trans.matriz[1][2] = 0;
                break;
            case 3:
                // Arriba
                trans.matriz[0][2] = 0;
                trans.matriz[1][2] = 10;
                break;
            case 4:
                //Abajo
                trans.matriz[0][2] = 0;
                trans.matriz[1][2] = -10;
                break;
        }
        //Ciclo que recorre y modifica las lineas
        for (int i = 0; i < lineas.size(); i++) {
            //Obtengo linea dibujada y edge
            Line2D linea = lineas.get(i);
            Edge lineae = edgesCasita.get(i);
            //Obtengo puntos de inicio y fin
            Point in = lineae.point1;
            Point fin = lineae.point2;
            //Convierto los puntos a Point2
            Point2 puntoi = new Point2(in.x, in.y, 1);
            Point2 puntof = new Point2(fin.x, fin.y, 1);
            //Realizo la multiplicaicion de matrices
            Point2 in1 = Matrix3x3.times(trans, puntoi);
            Point2 fin1 = Matrix3x3.times(trans, puntof);
            // Cambio coordenadas de linea
            linea.setLine(in1.x, in1.y, fin1.x, fin1.y);
            //Cambio coordenadas de Edge
            lineae.point1 = new Point(in1.x, in1.y);
            lineae.point2 = new Point(fin1.x, fin1.y);
            //Llamo al metodo que dibuja la linea
            setCoordinates(linea, g2d);
        }
    }

    public void rotar(Graphics2D g2d, int dir) {
        //Paso angulo a radianes
        double angle = Math.toRadians(10);
        Matrix3x3 trans = new Matrix3x3();
        //Creo matriz correspondiente
        switch (dir) {
            case 1:
                trans.matriz[0][0] = Math.cos(angle);
                trans.matriz[0][1] = -Math.sin(angle);
                trans.matriz[1][1] = Math.cos(angle);
                trans.matriz[1][0] = Math.sin(angle);
                trans.matriz[2][2] = 1;
                break;
            case 2:
                trans.matriz[0][0] = Math.cos(angle);
                trans.matriz[0][1] = Math.sin(angle);
                trans.matriz[1][1] = Math.cos(angle);
                trans.matriz[1][0] = -Math.sin(angle);
                trans.matriz[2][2] = 1;
                break;
        }

        for (int i = 0; i < lineas.size(); i++) {
            //Obtengo linea dibujada y edge
            Line2D linea = lineas.get(i);
            Edge lineae = edgesCasita.get(i);
            //Obtengo puntos de inicio y fin
            Point in = lineae.point1;
            Point fin = lineae.point2;
            //Convierto los puntos a Point2
            Point2 puntoi = new Point2(in.x, in.y, 1);
            Point2 puntof = new Point2(fin.x, fin.y, 1);
            //Realizo la multiplicaicion de matrices
            Point2 in1 = Matrix3x3.times(trans, puntoi);
            Point2 fin1 = Matrix3x3.times(trans, puntof);
            //Cambio coordenadas d elinea y de Edge
            linea.setLine(in1.x, in1.y, fin1.x, fin1.y);
            lineae.point1 = new Point(in1.x, in1.y);
            lineae.point2 = new Point(fin1.x, fin1.y);
            //Llamo al metodo que dibuja la linea
            setCoordinates(linea, g2d);
        }
    }

    public void escalar(Graphics2D g2d, int dir) {
        //Creo matriz correspondiente
        Matrix3x3 trans = new Matrix3x3();
        trans.matriz[2][2] = 1;
        switch (dir) {
            case 1:
                trans.matriz[0][0] = 1.1;
                trans.matriz[1][1] = 1.1;
                break;
            case 2:
                trans.matriz[0][0] = 0.9;
                trans.matriz[1][1] = 0.9;
                break;
        }
        //Llamo al metodo que haya el centroide y centra la figura para su escalamiento
        Point centroide = centroide(g2d);

        //Ciclo que escala la figura estando enn el centro
        for (int i = 0; i < edgesCasita.size(); i++) {
            //Obtengo linea
            Edge lineae = edgesCasita.get(i);
            //Obtengo puntos de inicio y fin
            Point in = lineae.point1;
            Point fin = lineae.point2;
            //Convierto a Point2
            Point2 puntoi = new Point2(in.x, in.y, 1);
            Point2 puntof = new Point2(fin.x, fin.y, 1);
            //Hago la multiplicaicon de matrices
            Point2 in1 = Matrix3x3.times(trans, puntoi);
            Point2 fin1 = Matrix3x3.times(trans, puntof);
            //Actualizo puntos
            lineae.point1 = new Point(in1.x, in1.y);
            lineae.point2 = new Point(fin1.x, fin1.y);
        }
        //Llamo metodo que devuelve la figura a su posición original
        volverOriginal(g2d, centroide);

    }

    private Point centroide(Graphics2D g2d) {
        //Hallo datos paraencontrar el centroide
        double xMax = Integer.MIN_VALUE;
        double yMax = Integer.MIN_VALUE;
        double yMin = Integer.MAX_VALUE;
        double xMin = Integer.MAX_VALUE;
        for (int i = 0; i < edgesCasita.size(); i++) {
            Edge a = edgesCasita.get(i);
            if (a.point1.x > xMax) {
                xMax = a.point1.x;
            }
            if (a.point2.x > xMax) {
                xMax = a.point2.x;
            }
            if (a.point1.y > yMax) {
                yMax = a.point1.y;
            }
            if (a.point2.y > yMax) {
                yMax = a.point1.y;
            }
            if (a.point1.x < xMin) {
                xMin = a.point1.x;
            }
            if (a.point2.x < xMin) {
                xMin = a.point2.x;
            }
            if (a.point1.y < yMin) {
                yMin = a.point1.y;
            }
            if (a.point2.y < yMin) {
                yMin = a.point2.y;
            }
        }
        //Creo centroide
        Point centroide = new Point(xMax - ((xMax - xMin) / 2), yMax - ((yMax - yMin) / 2));
        //Creo matriz de translacion para el origen
        Matrix3x3 trans = new Matrix3x3();
        trans.matriz[0][0] = 1;
        trans.matriz[1][1] = 1;
        trans.matriz[2][2] = 1;
        trans.matriz[0][2] = -centroide.x;
        trans.matriz[1][2] = -centroide.y;
        //Ciclo que translada la figura al centro
        for (int i = 0; i < edgesCasita.size(); i++) {
            Line2D linea = lineas.get(i);
            Edge lineae = edgesCasita.get(i);
            Point in = lineae.point1;
            Point fin = lineae.point2;
            Point2 puntoi = new Point2(in.x, in.y, 1);
            Point2 puntof = new Point2(fin.x, fin.y, 1);
            Point2 in1 = Matrix3x3.times(trans, puntoi);
            Point2 fin1 = Matrix3x3.times(trans, puntof);
            linea.setLine(in1.x, in1.y, fin1.x, fin1.y);
            lineae.point1 = new Point(in1.x, in1.y);
            lineae.point2 = new Point(fin1.x, fin1.y);
        }
        return centroide;
    }

    private void volverOriginal(Graphics2D g2d, Point centroide) {
        //Creo matriz de translación
        Matrix3x3 trans = new Matrix3x3();
        trans.matriz[0][0] = 1;
        trans.matriz[1][1] = 1;
        trans.matriz[2][2] = 1;
        trans.matriz[0][2] = centroide.x;
        trans.matriz[1][2] = centroide.y;
        //Desplazo la figura a donde se encontraba
        for (int i = 0; i < edgesCasita.size(); i++) {
            Line2D linea = lineas.get(i);
            Edge lineae = edgesCasita.get(i);
            Point in = lineae.point1;
            Point fin = lineae.point2;
            Point2 puntoi = new Point2(in.x, in.y, 1);
            Point2 puntof = new Point2(fin.x, fin.y, 1);
            Point2 in1 = Matrix3x3.times(trans, puntoi);
            Point2 fin1 = Matrix3x3.times(trans, puntof);
            linea.setLine(in1.x, in1.y, fin1.x, fin1.y);
            lineae.point1 = new Point(in1.x, in1.y);
            lineae.point2 = new Point(fin1.x, fin1.y);
            setCoordinates(linea, g2d);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int tecla = e.getKeyCode();
        //Determina que operacion hacer y en que dirección
        switch (tecla) {
            case KeyEvent.VK_UP:
                //Translacion arriba
                rotacion = false;
                translacion = true;
                escalamiento = false;
                direccion = 3;
                break;
            case KeyEvent.VK_DOWN:
                //Translacion abajo
                rotacion = false;
                translacion = true;
                escalamiento = false;
                direccion = 4;
                break;
            case KeyEvent.VK_RIGHT:
                //Translacion derecha
                rotacion = false;
                translacion = true;
                escalamiento = false;
                direccion = 1;
                break;
            case KeyEvent.VK_LEFT:
                //Translacion izquierda
                rotacion = false;
                translacion = true;
                escalamiento = false;
                direccion = 2;
                break;
            case KeyEvent.VK_A:
                //Rotacion anti-clockwise
                translacion = false;
                rotacion = true;
                escalamiento = false;
                direccion = 1;
                break;
            case KeyEvent.VK_D:
                //Rotacion clockwise
                translacion = false;
                rotacion = true;
                escalamiento = false;
                direccion = 2;
                break;
            case KeyEvent.VK_W:
                //Agrandar
                translacion = false;
                rotacion = false;
                escalamiento = true;
                direccion = 1;
                break;
            case KeyEvent.VK_S:
                //Encoger
                translacion = false;
                rotacion = false;
                escalamiento = true;
                direccion = 2;
                break;
            default:
                break;
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Casita");
        CasitaDib dib = new CasitaDib();
        f.add(dib);
        f.setSize(700, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    /**
     * Método que dibuja la linea a partir de las coordenadas cartesianas
     */
    private void setCoordinates(Line2D linea, Graphics2D g2d) {
        int origeny = getHeight() / 2;
        int origenx = (getWidth() / 2);

        double x1 = linea.getX1() + origenx;
        double y1 = origeny - linea.getY1();
        double x2 = linea.getX2() + origenx;
        double y2 = origeny - linea.getY2();
        linea.setLine(x1, y1, x2, y2);
        g2d.draw(linea);
    }
}
