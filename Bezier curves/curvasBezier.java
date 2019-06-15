package transformaciones3D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JFrame;
/**
 * Implementación del reto propuesto en computación gráfica para dibujar una superficie curva 
 * basandose en la teoria vista en clase de curvas de Bezier
 * 
 * Construido a base de lo que se tenia anteriormente pero refactorizado para ahorrar lineas
 * @author Luisa María Vásquez, Isabela Muriel
 * @version 17/03/2019
 */
public class curvasBezier extends JPanel implements KeyListener {
    Graphics2D g2d;
    int w, h;
    ArrayList<Edge3D> edgesPlano;    //Lineas que conforman el plano
    ArrayList<Edge3D> edgesDibujados;   //Lineas dibujadas desde el punto de la camara
    double distancia;    // Distancia desde la que se observa

    double radio = 500;  // Distancia de la camara al objeto
    
    //Angulos de la camara
    double theta = 0;
    double phi = 0;
    
    //Posicion de la camara
    double xCamera = 0;
    double yCamera = 0;
    double zCamera = 100;

    Point3[][] puntos = { 
                            { 
                                new Point3(-150.0, -100.0, 1100.0, 1),
                                new Point3(-50.0, 0.0, 1100.0, 1),
                                new Point3(50.0, 0.0, 1100.0, 1),
                                new Point3(150.0, 100.0, 1100.0, 1),
                            },
                            {
                                new Point3(-150.0, 0.0, 1000.0, 1),
                                new Point3(-50.0, 0.0, 1000.0, 1),
                                new Point3(50.0, 0.0, 1000.0, 1),
                                new Point3(150.0, 0.0, 1000.0, 1)
                            },
                            {
                                new Point3(-150.0, -100.0, 900.0, 1),
                                new Point3(-50.0, 0.0, 900.0, 1),
                                new Point3(50.0, 0.0, 900.0, 1),
                                new Point3(150.0, 100.0, 900.0, 1)
                            }
                        };

    public curvasBezier() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.edgesPlano = new ArrayList<>();
        this.edgesDibujados = new ArrayList<>();
        this.distancia = -800;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        
        Dimension size = getSize();
        Insets insets = getInsets();
        w =  size.width - insets.left - insets.right;
        h =  size.height - insets.top - insets.bottom;
        
        g2d.setColor(new Color(172, 4, 172));
        puntosControl();
    }

    @Override
    public void keyPressed(KeyEvent e){
        int tecla = e.getKeyCode();
        switch(tecla){
            case KeyEvent.VK_RIGHT:    // Trasladar X en sentido positivo
                transladar(10,0,0);
                break;
            case KeyEvent.VK_LEFT:     // Trasladar X en sentido negativo
                transladar(-10,0,0);
                break;
            case KeyEvent.VK_UP:        // Trasladar Y en sentido positivo
                transladar(0,10,0);
                break;
            case KeyEvent.VK_DOWN:      // Trasladar Y en sentido negativo
                transladar(0,-10,0);
                break;
            case KeyEvent.VK_M:         // Trasladar en Z en sentido positivo
                transladar(0,0,10);
                break;
            case KeyEvent.VK_N:         // Trasladar en Z en sentido negativo
                transladar(0,0,-10);
                break;
            case KeyEvent.VK_A:         // Rotar en X en sentido positivo
                rotarX(10);
                break;
            case KeyEvent.VK_D:         // Rotar en X en sentido negativo
                rotarX(-10);
                break;
            case KeyEvent.VK_W:         // Rotar en Y en sentido positivo
                rotarY(10);
                break;
            case KeyEvent.VK_S:         // Rotar en Y en sentido negativo
                rotarY(-10);
                break;
            case KeyEvent.VK_Q:         // Rotar en Z en sentido positivo
                rotarZ(10);
                break;
            case KeyEvent.VK_E:         // Rotar en Z en sentido negativo
                rotarZ(-10);
                break;
            case KeyEvent.VK_J:         // Escalar en X en sentido positivo
                scale(1.1, 1, 1);
                break;
            case KeyEvent.VK_L:         // Escalar en X en sentido negativo
                scale(0.9, 1, 1);
                break;
            case KeyEvent.VK_I:         // Escalar en Y en sentido positivo
                scale(1, 1.1, 1);
                break;
            case KeyEvent.VK_K:         // Escalar en Y en sentido negativo
                scale(1, 0.9, 1);
                break;
            case KeyEvent.VK_O:         // Escalar en Z en sentido positivo
                scale(1, 1, 1.1);
                break;
            case KeyEvent.VK_U:         // Escalar en Z en sentido negativo
                scale(1, 1, 0.9);
                break;
            case KeyEvent.VK_H:         // Rebaja la perspectiva
                planoProyeccion(distancia -= 20);
                break;
            case KeyEvent.VK_G:         // Aumenta la perspectiva
                planoProyeccion(distancia += 20);
                break;
            case KeyEvent.VK_R:         // Rotar camara a la izquierda
                theta -= 10;
                break;
            case KeyEvent.VK_Y:         // Rotar camara a la derecha
                theta += 10;
                break;
            case KeyEvent.VK_T:         // Rotar camara hacia abajo
                if (phi > -79) {
                    phi -= 10;          //Aumento 10 hasta 80 
                }else if (phi>-89){
                    phi-= 89+phi;       //De ahi en adelante aumento lo que falte para 89
                }
                break;
            case KeyEvent.VK_6:        // Rotar camara hacia arriba
                if (phi < 79) {
                    phi += 10;         //Aumento 10 hasta 80 
                }else if (phi<89){     //De ahi en adelante aumento lo que falte para 89
                    phi+=(89-phi);
                }
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Método que aplica el matriz de proyección
     * @param d  Distancia desde la que se observa la figura
    */
    public void planoProyeccion(double d){
        //Creo la matriz de tranformación
        Matrix4x4 mPerspective = new Matrix4x4();
        mPerspective.matriz[0][0] = 1;
        mPerspective.matriz[0][1] = 0;
        mPerspective.matriz[0][2] = 0;
        mPerspective.matriz[0][3] = 0;
        mPerspective.matriz[1][0] = 0;
        mPerspective.matriz[1][1] = 1;
        mPerspective.matriz[1][2] = 0;
        mPerspective.matriz[1][3] = 0;
        mPerspective.matriz[2][0] = 0;
        mPerspective.matriz[2][1] = 0;
        mPerspective.matriz[2][2] = 1;
        mPerspective.matriz[2][3] = 0;
        mPerspective.matriz[3][0] = 0;
        mPerspective.matriz[3][1] = 0;
        mPerspective.matriz[3][2] = (double)1/d;
        mPerspective.matriz[3][3] = 0;

        //Recorro las lineas que ve la camara y aplico la perspectiva
        for(Edge3D borde : edgesDibujados){
            //Obtengo los puntos
            Point3 p1 = borde.point1;
            Point3 p2 = borde.point2;
            //Los multiplico por la matriz de transformación
            p1 = Matrix4x4.times(mPerspective, p1);
            p2 = Matrix4x4.times(mPerspective, p2);
            // Normalizar puntos
            p1.normalize();
            p2.normalize();
           
            //Llamo al metodo que dibuja en coordenadas del panel
            Line2D line = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
            setCoordinates(line);
        }
        edgesDibujados.clear();
    }

    /**
     * Visualiza el objeto a traves de la camara
     */
    public void vistaCamara(){
        // Coordenadas de la camara
        double r = radio * Math.cos(Math.toRadians(phi));
        xCamera = r * Math.sin(Math.toRadians(theta));
        yCamera = radio * Math.sin(Math.toRadians(phi));
        zCamera = r * Math.cos(Math.toRadians(theta));

        // Centro del objeto
        Point3 centroide = centroide();

        // Ubico la camara mirando hacia el objeto
        xCamera += centroide.x;
        yCamera += centroide.y;
        zCamera += centroide.z;

        // Puntos para generar el vector lookAt
        double x = xCamera - centroide.x;
        double y = yCamera - centroide.y;
        double z = zCamera - centroide.z;

        // Vectores necesarios para generar la matriz de traslación de la camara
        Vector3 n = new Vector3(x, y, z).normalize();  // vector lookAt = N/|N|     N = vector objeto - vector camara
        Vector3 upVector = new Vector3(0, 1, 0);  // vector V
        Vector3 u = Vector3.crossProduct(upVector, n).normalize();   // u = (V x n) / (|V x n|)

        Vector3 v = Vector3.crossProduct(n, u); // v = n x u
        Vector3 p0 = new Vector3(xCamera, yCamera, zCamera);  // Punto inicial de la camara
        
        Matrix4x4 trans = new Matrix4x4();
        trans.matriz[0][0] = u.u;
        trans.matriz[0][1] = u.v;
        trans.matriz[0][2] = u.w;
        trans.matriz[0][3] = - Vector3.dotProduct(u, p0);
        trans.matriz[1][0] = v.u;
        trans.matriz[1][1] = v.v;
        trans.matriz[1][2] = v.w;
        trans.matriz[1][3] = - Vector3.dotProduct(v, p0);
        trans.matriz[2][0] = n.u;
        trans.matriz[2][1] = n.v;
        trans.matriz[2][2] = n.w;
        trans.matriz[2][3] = - Vector3.dotProduct(n, p0);
        trans.matriz[3][0] = 0;
        trans.matriz[3][1] = 0;
        trans.matriz[3][2] = 0;
        trans.matriz[3][3] = 1;

        // Realizo la rotación de la camara
        mult(trans);
        planoProyeccion(distancia);
    }

    /**
     * Traslada la figura 
     * @param dx   Numero de pixeles a cambiar en x
     * @param dy   Numero de pixeles a cambiar en y
     * @param dz   Numero de pixeles a cambiar en z
     */
    public void transladar(double dx, double dy, double dz){
        Matrix4x4 trans = new Matrix4x4();
        trans.matriz[0][0] = 1;
        trans.matriz[0][1] = 0;
        trans.matriz[0][2] = 0;
        trans.matriz[0][3] = dx;
        trans.matriz[1][0] = 0;
        trans.matriz[1][1] = 1;
        trans.matriz[1][2] = 0;
        trans.matriz[1][3] = dy;
        trans.matriz[2][0] = 0;
        trans.matriz[2][1] = 0;
        trans.matriz[2][2] = 1;
        trans.matriz[2][3] = dz;
        trans.matriz[3][0] = 0;
        trans.matriz[3][1] = 0;
        trans.matriz[3][2] = 0;
        trans.matriz[3][3] = 1;

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(trans, puntos[i][j]);
            }
        }
    }

    /**
     * Rota la figura en torno al eje x
     * @param dir  Angulo de rotación en grados
     */
    public void rotarX(int dir){
        double rad = dir * Math.PI/180;    // Conversion de grados a radianes

        Matrix4x4 mRotacion = new Matrix4x4();
        mRotacion.matriz[0][0] = 1;
        mRotacion.matriz[0][1] = 0;
        mRotacion.matriz[0][2] = 0;
        mRotacion.matriz[0][3] = 0;
        mRotacion.matriz[1][0] = 0;
        mRotacion.matriz[1][1] = Math.cos(rad);
        mRotacion.matriz[1][2] = - Math.sin(rad);
        mRotacion.matriz[1][3] = 0;
        mRotacion.matriz[2][0] = 0;
        mRotacion.matriz[2][1] = Math.sin(rad);
        mRotacion.matriz[2][2] = Math.cos(rad);
        mRotacion.matriz[2][3] = 0;
        mRotacion.matriz[3][0] = 0;
        mRotacion.matriz[3][1] = 0;
        mRotacion.matriz[3][2] = 0;
        mRotacion.matriz[3][3] = 1;

        Point3 centroide = centroide();
        transladar(- centroide.x, - centroide.y, - centroide.z);   // Traslada la figura al origen

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mRotacion, puntos[i][j]);
            }
        }
        transladar(centroide.x, centroide.y, centroide.z);  // Devuelve la figura a su posición original
    }

    /**
     * Rota la figura en torno al eje y
     * @param dir  Angulo de rotación en grados
     */
    public void rotarY(int dir){
        double rad = dir * Math.PI/180;    // Conversion de diros a radianes

        Matrix4x4 mRotacion = new Matrix4x4();
        mRotacion.matriz[0][0] = Math.cos(rad);
        mRotacion.matriz[0][1] = 0;
        mRotacion.matriz[0][2] = Math.sin(rad);
        mRotacion.matriz[0][3] = 0;
        mRotacion.matriz[1][0] = 0;
        mRotacion.matriz[1][1] = 1;
        mRotacion.matriz[1][2] = 0;
        mRotacion.matriz[1][3] = 0;
        mRotacion.matriz[2][0] = - Math.sin(rad);
        mRotacion.matriz[2][1] = 0;
        mRotacion.matriz[2][2] = Math.cos(rad);
        mRotacion.matriz[2][3] = 0;
        mRotacion.matriz[3][0] = 0;
        mRotacion.matriz[3][1] = 0;
        mRotacion.matriz[3][2] = 0;
        mRotacion.matriz[3][3] = 1;

        Point3 centroide = centroide();

        transladar(- centroide.x, - centroide.y, - centroide.z);   // Traslada la figura al origen

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mRotacion, puntos[i][j]);
            }
        }

        transladar(centroide.x, centroide.y, centroide.z);  // Devuelve la figura a su posición original
    }

    /**
     * Rota la figura en torno al eje z
     * @param dir  Angulo de rotación en grados
     */
    public void rotarZ(int dir){
        double rad = dir* Math.PI/180;    // Conversion de grados a radianes

        Matrix4x4 mRotacion = new Matrix4x4();
        mRotacion.matriz[0][0] = Math.cos(rad);
        mRotacion.matriz[0][1] = - Math.sin(rad);
        mRotacion.matriz[0][2] = 0;
        mRotacion.matriz[0][3] = 0;
        mRotacion.matriz[1][0] = Math.sin(rad);
        mRotacion.matriz[1][1] = Math.cos(rad);
        mRotacion.matriz[1][2] = 0;
        mRotacion.matriz[1][3] = 0;
        mRotacion.matriz[2][0] = 0;
        mRotacion.matriz[2][1] = 0;
        mRotacion.matriz[2][2] = 1;
        mRotacion.matriz[2][3] = 0;
        mRotacion.matriz[3][0] = 0;
        mRotacion.matriz[3][1] = 0;
        mRotacion.matriz[3][2] = 0;
        mRotacion.matriz[3][3] = 1;

        Point3 centroide = centroide();

        transladar(- centroide.x, - centroide.y, - centroide.z);   // Traslada la figura al origen

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mRotacion, puntos[i][j]);
            }
        }

        transladar(centroide.x, centroide.y, centroide.z);  // Devuelve la figura a su posición original
    }

    /**
     * Aumenta el tamaño de la figura
     * @param sx  Numero de pixeles a aumentar en x
     * @param sy  Numero de pixeles a aumentar en y
     * @param sz  Numero de pixeles a aumentar en z
     */
    public void scale(double sx, double sy, double sz){
        Matrix4x4 mEscalamiento = new Matrix4x4();
        mEscalamiento.matriz[0][0] = sx;
        mEscalamiento.matriz[0][1] = 0;
        mEscalamiento.matriz[0][2] = 0;
        mEscalamiento.matriz[0][3] = 0;
        mEscalamiento.matriz[1][0] = 0;
        mEscalamiento.matriz[1][1] = sy;
        mEscalamiento.matriz[1][2] = 0;
        mEscalamiento.matriz[1][3] = 0;
        mEscalamiento.matriz[2][0] = 0;
        mEscalamiento.matriz[2][1] = 0;
        mEscalamiento.matriz[2][2] = sz;
        mEscalamiento.matriz[2][3] = 0;
        mEscalamiento.matriz[3][0] = 0;
        mEscalamiento.matriz[3][1] = 0;
        mEscalamiento.matriz[3][2] = 0;
        mEscalamiento.matriz[3][3] = 1;

        Point3 centroide = centroide();

        transladar(- centroide.x, - centroide.y, - centroide.z);   // Traslada la figura al origen

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mEscalamiento, puntos[i][j]);
            }
        }

        transladar(centroide.x, centroide.y, centroide.z);    // Devuelve la figura a su posición original  
    }

    /**
     * Encuentra el centroide de la figura
     * @return Punto centroide
     */
    public Point3 centroide(){
        double xMax = Integer.MIN_VALUE;
        double yMax = Integer.MIN_VALUE;
        double yMin = Integer.MAX_VALUE;
        double xMin = Integer.MAX_VALUE;
        double zMax = Integer.MIN_VALUE;
        double zMin = Integer.MAX_VALUE;
        //Recorre cada punto de la figura y busca el maximo y minimo de cada variable
        for (int i = 0; i < edgesPlano.size(); ++i) {
            Edge3D borde = edgesPlano.get(i);
            if (borde.point1.x > xMax) {
                xMax = borde.point1.x;
            }
            if (borde.point2.x > xMax) {
                xMax = borde.point2.x;
            }
            if (borde.point1.y > yMax) {
                yMax = borde.point1.y;
            }
            if (borde.point2.y > yMax) {
                yMax = borde.point1.y;
            }
            if (borde.point1.x < xMin) {
                xMin = borde.point1.x;
            }
            if (borde.point2.x < xMin) {
                xMin = borde.point2.x;
            }
            if (borde.point1.y < yMin) {
                yMin = borde.point1.y;
            }
            if (borde.point2.y < yMin) {
                yMin = borde.point2.y;
            }
            if (borde.point1.z > zMax) {
                zMax = borde.point1.z;
            }
            if (borde.point2.z > zMax) {
                zMax = borde.point2.z;
            }
            if (borde.point1.z < zMin) {
                zMin = borde.point1.z;
            }
            if (borde.point2.z < zMin) {
                zMin = borde.point2.z;
            }
        }
        // Crea el punto centroide
        Point3 centroid = new Point3(xMax - ((xMax - xMin) / 2), yMax - ((yMax - yMin) / 2), zMax - ((zMax - zMin) / 2), 1);
        return centroid;
    }

    /**
     * Multiplica todos los puntos del objeto por la matriz y los almacena en la lista de bordes a dibujar
     * @param mat    Matriz por la que se multiplican los puntos
     */
    private void mult(Matrix4x4 mat){
        //Método que multiplica cada punto del dibujo por la matriz dada
        for (Edge3D borde : edgesPlano) {
            //Obtengo puntos de inicio y fin de cada punto
            Point3 in = borde.point1;
            Point3 fin = borde.point2;
            //Multiplico los puntos por matriz
            Point3 punto1 = Matrix4x4.times(mat, in);
            Point3 punto2 = Matrix4x4.times(mat, fin);

            edgesDibujados.add(new Edge3D(punto1, punto2));
        }
    }

    /**
     * Dibuja las lineas de la figura
     * @param linea   Linea a dibujar
     */
    public void setCoordinates(Line2D linea){
        int borderX = getWidth() / 2;
        int borderY = getHeight() / 2;

        double x0 = linea.getX1() + borderX;
        double y0 = borderY - linea.getY1();
        double x1 = linea.getX2() + borderX;
        double y1 = borderY - linea.getY2();

        linea.setLine(x0, y0, x1, y1);
        g2d.draw(linea);
    }

    /**
     * C(n, k) = n! / (k!(n-k)!)
     * @param n
     * @param k
     * @return result  -->  Valor de C(n,u)
     */
    private Double C(int n, int k){
        Double result = factorial(n) / (factorial(k) * factorial(n - k));
        return result;
    }

    /**
     * BEZ_k,n(u) = C(n, k)u^k(1-u)^(n-k)
     * @param k
     * @param n
     * @param u
     * @return result --> Valor de BEZ_k,n(u)
     */
    private Double BEZ(int k, int n, double u){
        double result = C(n, k) * Math.pow(u, k) * Math.pow(1 - u, n - k);
        return result;
    }

    /**
     * Toma los puntos de control y calcula los puntos de la superficie
     * @param u
     * @param v
     * @return punto --> punto calculado utilizando la funcion de Bezier
     */
    private Point3 P(double u, double v){
        int n = puntos.length - 1;     // Numero de filas
        int m = puntos[0].length - 1;  // Numero de columnas

        Point3 punto = new Point3(0, 0, 0, 1);
        for(int j = 0; j <= m; ++j){
            for(int k = 0; k <= n; ++k){
                punto.x += (puntos[k][j].x * BEZ(j, m, u) * BEZ(k, n, v));
                punto.y += (puntos[k][j].y * BEZ(j, m, u) * BEZ(k, n, v));
                punto.z += (puntos[k][j].z * BEZ(j, m, u) * BEZ(k, n, v));
            }
        }
        return punto;
    }

    /**
     * Toma los puntos por los que pasa la superficie y añade a la lista de bordes cada uno de las lineas de la superficie
     */
    private void puntosControl(){
        edgesPlano.clear();    // Limpia la lista de bordes para actualizarla con los nuevos puntos
        for(double u = 0; u < 1; u += 0.01){
            for(double v = 0; v < 1; v += 0.01){
                Point3 p1 = P(u, v);  // Punto inicial de la linea
                Point3 p2 = P(u+0.025, v+0.025);   // Punto final de la linea
                edgesPlano.add(new Edge3D(p1, p2));   // Crea el nuevo borde y lo añade a la lista
            }
        }
        vistaCamara();    // Centra la camara en la superficie y le aplica la proyecta
    }

    /**
     * Calcula n!
     * @param n
     * @return result --> Resultado obtenido al calcular n!
     */
    private Double factorial(int n){
        double result = 1;
        for(; n > 0; --n){
            result *= n;
        }
        return result;
    }

    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Surface");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        curvasBezier curvas = new curvasBezier();
        frame.add(curvas);
        // Asignarle tamaño
        frame.setSize(900, 900);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
}