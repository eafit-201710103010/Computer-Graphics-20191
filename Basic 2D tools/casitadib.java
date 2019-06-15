package compgraf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Implementacion del ejercicio propuesto en la clase de computacion grafica
 * para leer instrucciones desde un txt y dibujar una figura segun estas.
 *
 * @version 13/02/2019
 * @author Luisa Maria Vasquez
 */
class CasitaDib extends JPanel {

    //Dirección archivo txt
    private static final String FILENAME = "C:\\Users\\luisa\\Documents\\NetBeansProjects\\Computacion Grafica\\src\\compgraf\\casita.txt";
    private static final ArrayList<Point> puntos = new ArrayList<>();
    private static final ArrayList<Line2D> lineas = new ArrayList<>();
    private static final ArrayList<Edge> edges = new ArrayList<>();

    CasitaDib() {
        setPreferredSize(new Dimension(700, 500));
        setBackground(Color.WHITE);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        leerArchivo(g2d);
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

            // Dibujar los ejes
            Line2D ejey = new Line2D.Double(origenx, 0, origenx, getHeight());
            Line2D ejex = new Line2D.Double(0, origeny, getWidth(), origeny);
            g2d.draw(ejex);
            g2d.draw(ejey);

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
                Point a = new Point(x1, y1);
                Point b = new Point(x2, y2);
                Edge edge = new Edge(a, b);
                // Dibujar la linea y agregarlo al arreglo
                Line2D linea = new Line2D.Double(x1, y1, x2, y2);
                lineas.add(linea);
                g2d.draw(linea);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
