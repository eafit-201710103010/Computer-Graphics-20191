package transformaciones3D;

import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Implementacion del ejercicio propuesto en la clase de computacion grafica
 * para realizar multiples transformaciones3D a un dibujo
 *
 * @version 27/02/2019
 * @author Luisa Maria Vasquez
 */
class CasitaDib extends JPanel
        implements KeyListener {

    //Dirección archivo txt
    private static final String FILENAME = "C:\\Users\\luisa\\Documents\\NetBeansProjects\\Transformaciones\\src\\transformaciones3D\\casita.txt\\";
    // ArrayList de los puntos inciiales de la figura
    private static final ArrayList<Point3> puntos = new ArrayList<>();
    //ArrayList de tipo Edge que representan als lineas dibujadas
    private static final ArrayList<Edge3D> edgesCasita = new ArrayList<>();

    //Variable que define cual tranfromación realizar
    int op = 0;
    //Variable que da la direccion de la tranformación
    int direccion = 0;
    //Variable para solo leer una vez
    boolean leido = false;
    //Distancia del plano
    double dis = -500;

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
        //La tabal de surf se dibujara en morado
        g2d.setColor(new Color(172, 4, 172));

        //Leer el archivo de texto
        if (!leido) {
            leerArchivo(g2d);
            dibujar(g2d);
        }
        // Seleccionar que transformacion voy a hacer
        switch (op) {
            case 1:
                transladar(g2d, direccion);
                break;
            case 2:
                rotar(g2d, direccion);
                break;
            case 3:
                escalar(g2d, direccion);
                break;
            case 4:
                planoProyeccion(g2d, direccion);
                break;
            default:
                break;
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
                double x = Integer.parseInt(xy[0]);
                double y = Integer.parseInt(xy[1]);
                double z = Integer.parseInt(xy[2]);
                Point3 punto = new Point3(x, y, z, 1);
                puntos.add(punto);
            }
            //Leer cantidad de lineas 
            int clineas;
            sCurrentLine = br.readLine();
            clineas = Integer.parseInt(sCurrentLine);

            //Leer y dibujar  las lineas
            for (int i = 0; i < clineas; i++) {
                sCurrentLine = br.readLine();
                String[] edges = sCurrentLine.split(" ");

                // Obtener coordenadas de inicio y fin 
                double x1 = puntos.get(Integer.parseInt(edges[0])).x;
                double y1 = puntos.get(Integer.parseInt(edges[0])).y;
                double z1 = puntos.get(Integer.parseInt(edges[0])).z;
                double x2 = puntos.get(Integer.parseInt(edges[1])).x;
                double y2 = puntos.get(Integer.parseInt(edges[1])).y;
                double z2 = puntos.get(Integer.parseInt(edges[1])).z;

                // Crear la linea y agregarla al errglo con su respectivo punto de incio y fin
                Point3 a = new Point3(x1, y1, z1, 1);
                Point3 b = new Point3(x2, y2, z2, 1);
                Edge3D edge = new Edge3D(a, b);
                edgesCasita.add(edge);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        leido = true;
    }

    public void transladar(Graphics2D g2d, int dir) {
        //Creo matriz de tranformacion
        Matrix4x4 trans = new Matrix4x4();
        trans.matriz[0][0] = 1;
        trans.matriz[1][1] = 1;
        trans.matriz[2][2] = 1;
        trans.matriz[3][3] = 1;

        //Asigno valores segun dirección de tranformación
        switch (dir) {
            case 1:
                // Derecha
                trans.matriz[0][3] = 10;
                trans.matriz[1][3] = 0;
                trans.matriz[2][3] = 0;
                break;
            case 2:
                // Izquierda 
                trans.matriz[0][3] = -10;
                trans.matriz[1][3] = 0;
                trans.matriz[2][3] = 0;
                break;
            case 3:
                // Arriba
                trans.matriz[0][3] = 0;
                trans.matriz[1][3] = 10;
                trans.matriz[2][3] = 0;
                break;
            case 4:
                //Abajo
                trans.matriz[0][3] = 0;
                trans.matriz[1][3] = -10;
                trans.matriz[2][3] = 0;
                break;
            case 5:
                //Alejar
                trans.matriz[0][3] = 0;
                trans.matriz[1][3] = 0;
                trans.matriz[2][3] = -10;
                break;
            case 6:
                //Acercar
                trans.matriz[0][3] = 0;
                trans.matriz[1][3] = 0;
                trans.matriz[2][3] = 10;
                break;
        }
        //Llamo metodo que multiplica cada punto por la matriz
        mult(trans);
        //Dibujo la figura resultante
        dibujar(g2d);
    }

    public void rotar(Graphics2D g2d, int dir) {
        //Paso angulo a radianes
        double angle = Math.toRadians(10);
        Matrix4x4 trans = new Matrix4x4();
        //Creo matriz correspondiente
        switch (dir) {
            case 1:
                //Rotacion X anti clockwise
                trans.matriz[0][0] = 1;
                trans.matriz[1][1] = Math.cos(angle);
                trans.matriz[1][2] = -Math.sin(angle);
                trans.matriz[2][2] = Math.cos(angle);
                trans.matriz[2][1] = Math.sin(angle);
                trans.matriz[3][3] = 1;
                break;
            case 2:
                //Rotacion X clockwise
                trans.matriz[0][0] = 1;
                trans.matriz[1][1] = Math.cos(angle);
                trans.matriz[1][2] = Math.sin(angle);
                trans.matriz[2][2] = Math.cos(angle);
                trans.matriz[2][1] = -Math.sin(angle);
                trans.matriz[3][3] = 1;
                break;

            case 3:
                //Rotacion anti-clockwise Eje Y
                trans.matriz[1][1] = 1;
                trans.matriz[0][0] = Math.cos(angle);
                trans.matriz[2][0] = -Math.sin(angle);
                trans.matriz[2][2] = Math.cos(angle);
                trans.matriz[0][2] = Math.sin(angle);
                trans.matriz[3][3] = 1;
                break;

            case 4:
                //Rotacion clockwise Eje Y
                trans.matriz[1][1] = 1;
                trans.matriz[0][0] = Math.cos(angle);
                trans.matriz[2][0] = Math.sin(angle);
                trans.matriz[2][2] = Math.cos(angle);
                trans.matriz[0][2] = -Math.sin(angle);
                trans.matriz[3][3] = 1;
                break;
            case 5:
                //Rotacion anti- clockwise Z
                trans.matriz[2][2] = 1;
                trans.matriz[0][0] = Math.cos(angle);
                trans.matriz[0][1] = -Math.sin(angle);
                trans.matriz[1][1] = Math.cos(angle);
                trans.matriz[1][0] = Math.sin(angle);
                trans.matriz[3][3] = 1;
                break;
            case 6:
                //Rotacion clockwise Z
                trans.matriz[2][2] = 1;
                trans.matriz[0][0] = Math.cos(angle);
                trans.matriz[0][1] = Math.sin(angle);
                trans.matriz[1][1] = Math.cos(angle);
                trans.matriz[1][0] = -Math.sin(angle);
                trans.matriz[3][3] = 1;
                break;
        }
        //Hallo centroide y muevo la figura al centro
        Point3 centroide = centroide(g2d);
        //Hago la multpkicaicond ematrices, rotando la figura
        mult(trans);
        //Devuelvo la figura a su posicion original
        volverOriginal(g2d, centroide);
        //Dibujo la figura resultante
        dibujar(g2d);
    }

    public void escalar(Graphics2D g2d, int dir) {
        //Creo matriz de tranformacion
        Matrix4x4 trans = new Matrix4x4();
        trans.matriz[3][3] = 1;
        //Asigno valores a la matriz segun la direccion 
        switch (dir) {
            case 1:
                //Agrandar x
                trans.matriz[0][0] = 1.1;
                trans.matriz[1][1] = 1;
                trans.matriz[2][2] = 1;
                break;
            case 2:
                //Encoger X
                trans.matriz[0][0] = 0.9;
                trans.matriz[1][1] = 1;
                trans.matriz[2][2] = 1;
                break;
            case 3:
                //Agrandar Y
                trans.matriz[0][0] = 1;
                trans.matriz[1][1] = 1.1;
                trans.matriz[2][2] = 1;
                break;
            case 4:
                //Encoger Y
                trans.matriz[0][0] = 1;
                trans.matriz[1][1] = 0.9;
                trans.matriz[2][2] = 1;
                break;
            case 5:
                //Agrandar Z
                trans.matriz[0][0] = 1;
                trans.matriz[1][1] = 1;
                trans.matriz[2][2] = 1.1;
                break;
            case 6:
                //Encoger Z
                trans.matriz[0][0] = 1;
                trans.matriz[1][1] = 1;
                trans.matriz[2][2] = 0.9;
                break;

        }

        //Hallo centroide y muevo la figura al centro
        Point3 centroide = centroide(g2d);
        //Hago la multpkicaicond ematrices, rotando la figura
        mult(trans);
        //Devuelvo la figura a su posicion original
        volverOriginal(g2d, centroide);
        //Dibujo la figura resultante
        dibujar(g2d);
    }

    private Point3 centroide(Graphics2D g2d) {
        //Variables iniciales para hallar centroide
        double xMax = Integer.MIN_VALUE;
        double yMax = Integer.MIN_VALUE;
        double yMin = Integer.MAX_VALUE;
        double xMin = Integer.MAX_VALUE;
        double zMax = Integer.MIN_VALUE;
        double zMin = Integer.MAX_VALUE;
        //Recorro cada punto buscando el maximo y minimo de cada variable
        for (int i = 0; i < edgesCasita.size(); i++) {
            Edge3D a = edgesCasita.get(i);
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
            if (a.point1.z > zMax) {
                zMax = a.point1.z;
            }
            if (a.point2.z > zMax) {
                zMax = a.point2.z;
            }
            if (a.point1.z < zMin) {
                zMin = a.point1.z;
            }
            if (a.point2.z < zMin) {
                zMin = a.point2.z;
            }
        }
        //Creo centroide
        Point3 centroide = new Point3(xMax - ((xMax - xMin) / 2), yMax - ((yMax - yMin) / 2), zMax - ((zMax - zMin) / 2), 1);
        //Creo matriz de translacion hacia el centro
        Matrix4x4 trans = new Matrix4x4();
        trans.matriz[0][0] = 1;
        trans.matriz[1][1] = 1;
        trans.matriz[2][2] = 1;
        trans.matriz[3][3] = 1;
        trans.matriz[0][3] = -centroide.x;
        trans.matriz[1][3] = -centroide.y;
        trans.matriz[2][3] = -centroide.z;
        //Realizo la multiplicaiconde la matriz por cada punto
        mult(trans);
        return centroide;
    }

    private void volverOriginal(Graphics2D g2d, Point3 centroide) {
        //Creo matriz de translación
        Matrix4x4 trans = new Matrix4x4();
        trans.matriz[0][0] = 1;
        trans.matriz[1][1] = 1;
        trans.matriz[2][2] = 1;
        trans.matriz[3][3] = 1;
        trans.matriz[0][3] = centroide.x;
        trans.matriz[1][3] = centroide.y;
        trans.matriz[2][3] = centroide.z;
        //Desplazo la figura a donde se encontraba
        mult(trans);

    }

    private void planoProyeccion(Graphics2D g2d, int dir) {
        //Cambio dir segun direccion (acercar o alejar)
        switch (dir) {
            case 1:
                dis += 20;
                break;
            case 2:
                dis -= 20;
        }
        //Dibujo resultado
        dibujar(g2d);
    }

    private void dibujar(Graphics2D g2d) {
        //Creo matriz de transformación para perspectiva
        Matrix4x4 mat = new Matrix4x4();
        mat.matriz[0][0] = 1;
        mat.matriz[1][1] = 1;
        mat.matriz[2][2] = 1;
        double val = 1 / dis;
        mat.matriz[3][2] = val;

        //Aplico perspectiva a cada punto
        for (Edge3D edge : edgesCasita) {
            //Obtengo puntos
            Point3 a = edge.point1;
            Point3 b = edge.point2;
            //Los multiplico por la matriz
            Point3 a1 = Matrix4x4.times(mat, a);
            Point3 b1 = Matrix4x4.times(mat, b);
            //Los normalizo
            a1.normalize();
            b1.normalize();
            //Los asigno a una linea
            Line2D line = new Line2D.Double(a1.x, a1.y, b1.x, b1.y);
            //Llamo al metodo que los dibuja con coordenadas del panel
            setCoordinates(line, g2d);
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
                op = 1;
                direccion = 3;
                break;
            case KeyEvent.VK_DOWN:
                //Translacion abajo
                op = 1;
                direccion = 4;
                break;
            case KeyEvent.VK_RIGHT:
                //Translacion derecha
                op = 1;
                direccion = 1;
                break;
            case KeyEvent.VK_LEFT:
                //Translacion izquierda
                op = 1;
                direccion = 2;
                break;
            case KeyEvent.VK_N:
                //Translacion Atras
                op = 1;
                direccion = 5;
                break;
            case KeyEvent.VK_M:
                //Translacion Adelante
                op = 1;
                direccion = 6;
                break;
            case KeyEvent.VK_A:
                //Rotacion anti-clockwise Eje X                
                op = 2;
                direccion = 1;
                break;
            case KeyEvent.VK_D:
                //Rotacion clockwise Eje X
                op = 2;
                direccion = 2;
                break;
            case KeyEvent.VK_W:
                //Rotacion anti-clockwise Eje Y
                op = 2;
                direccion = 3;
                break;
            case KeyEvent.VK_S:
                //Rotación clockwisse Eje Y
                op = 2;
                direccion = 4;
                break;
            case KeyEvent.VK_Q:
                //Rotación anti-clockwisse Eje Z
                op = 2;
                direccion = 5;
                break;
            case KeyEvent.VK_E:
                //Rotación clockwise Eje Z
                op = 2;
                direccion = 6;
                break;
            case KeyEvent.VK_J:
                //Agrandamiento X
                op = 3;
                direccion = 1;
                break;
            case KeyEvent.VK_L:
                //Encogimiento X
                op = 3;
                direccion = 2;
                break;
            case KeyEvent.VK_I:
                //Agrandamiento Y
                op = 3;
                direccion = 3;
                break;
            case KeyEvent.VK_K:
                //Encogimiento Y
                op = 3;
                direccion = 4;
                break;
            case KeyEvent.VK_O:
                //Agrandamiento Z
                op = 3;
                direccion = 5;
                break;
            case KeyEvent.VK_U:
                //Encogimiento Z
                op = 3;
                direccion = 6;
                break;
            case KeyEvent.VK_H:
                //Alejar plano de proyección
                op = 4;
                direccion = 1;
                break;
            case KeyEvent.VK_G:
                //Acercar plano de proyeccion
                op = 4;
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
        f.setTitle("Tabla");
        CasitaDib dib = new CasitaDib();
        f.add(dib);
        f.setSize(1000, 1000);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void setCoordinates(Line2D linea, Graphics2D g2d) {
        //Método que convierte a coordenadas del panel
        int origeny = getHeight() / 2;
        int origenx = (getWidth() / 2);
        double x1 = linea.getX1() + origenx;
        double y1 = origeny - linea.getY1();
        double x2 = linea.getX2() + origenx;
        double y2 = origeny - linea.getY2();
        linea.setLine(x1, y1, x2, y2);
        g2d.draw(linea);
    }

    private void mult(Matrix4x4 trans) {
        //Método que multiplica cada punto del dibujo por una matriz
        for (Edge3D lineae : edgesCasita) {
            //Obtengo puntos de inicio y fin de cada punto
            Point3 in = lineae.point1;
            Point3 fin = lineae.point2;
            //Multiplico los puntos por matriz
            Point3 in1 = Matrix4x4.times(trans, in);
            Point3 fin1 = Matrix4x4.times(trans, fin);
            //Asigno los nuevos puntos al arreglo
            lineae.point1 = new Point3(in1.x, in1.y, in1.z, in1.w);
            lineae.point2 = new Point3(fin1.x, fin1.y, fin1.z, fin1.w);
        }
    }
}
