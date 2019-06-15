

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import javax.swing.JPanel;
import javax.sound.midi.SysexMessage;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class LineClipping extends JPanel implements MouseListener {

    static int w, h, wr = 0, hr = 0;
    static int xMin;
    static int xMax;
    static int yMin;
    static int yMax;
    static int val = 0;
    static boolean line = false;
    static boolean rect = false;

    Line2D.Double linea1; // Diagonal
    Line2D.Double linea2; // Linea de Clipping

    public LineClipping() {
        linea1 = new java.awt.geom.Line2D.Double();
        linea2 = new java.awt.geom.Line2D.Double();
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        switch (val) {
        case 0:
            linea1.x1 = 0;
            linea1.y1 = 0;
            linea1.x2 = 0;
            linea1.y2 = 0;
            xMin = 0;
            yMin = 0;
            xMax = 0;
            yMax = 0;
            linea1.x1 = (int) e.getX();
            linea1.y1 = (int) e.getY();
            xMin = (int) linea1.x1;
            yMin = (int) linea1.y1;
            val++;
            break;

        case 1:
            linea1.x2 = (int) e.getX();
            linea1.y2 = (int) e.getY();

            xMax = (int) linea1.x2;
            yMax = (int) linea1.y2;

            wr = (int) linea1.x2 - (int) linea1.x1;
            hr = (int) linea1.y2 - (int) linea1.y1;
            val++;
            rect = true;
            break;
        case 2:
            linea2.x1 = (int) e.getX();
            linea2.y1 = (int) e.getY();
            val++;
            break;
        case 3:
            linea2.x2 = (int) e.getX();
            linea2.y2 = (int) e.getY();
            val = 0;
            line = true;
            break;
        }
        repaint();
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);

        // size es el tamaño de la ventana.
        Dimension size = getSize();
        // Insets son los bordes y los títulos de la ventana.
        Insets insets = getInsets();

        w = size.width - insets.left - insets.right;
        h = size.height - insets.top - insets.bottom;
        // Ejes
        g2d.drawLine(w / 2, 0, w / 2, h);
        g2d.drawLine(0, h / 2, w, h / 2);
        if (rect) {
            // Area de Clipping
            g2d.drawRect((int) linea1.x1, (int) linea1.y1, wr, hr);

            xMin = (int) linea1.x1;
            yMin = (int) linea1.y1;
            xMax = xMin + wr;
            yMax = yMin + hr;
            if (line) {
                int x0 = (int) linea2.x1;
                int y0 = (int) linea2.y1;
                int x1 = (int) linea2.x2;
                int y1 = (int) linea2.y2;

                int r[] = liangBarsky(g2d, x0, y0, x1, y1, h);

                if (r == null) {
                    g.setColor(Color.RED);
                    drawLine(g2d, x0, y0, x1, y1, h);
                } else {
                    g.setColor(Color.RED);
                    drawLine(g2d, x0, y0, r[0], r[1], h);
                    drawLine(g2d, r[2], r[3], x1, y1, h);
                    g.setColor(Color.GREEN);
                    drawLine(g2d, r[0], r[1], r[2], r[3], h);
                }
                rect = false;
                line = false;
            }
        }

    }

    public static int[] liangBarsky(Graphics2D g, int x0, int y0, int x1, int y1, int h) {
        double u1 = 0;
        double u2 = 1;

        int dx = x1 - x0;
        int dy = y1 - y0;

        int p[] = { -dx, dx, -dy, dy };
        int q[] = { x0 - xMin, xMax - x0, y0 - yMin, yMax - y0 };

        for (int i = 0; i < 4; ++i) {
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
        if (u1 > u2) {
            return null;
        }
        int nx0 = (int) (x0 + u1 * dx);
        int ny0 = (int) (y0 + u1 * dy);
        int nx1 = (int) (x0 + u2 * dx);
        int ny1 = (int) (y0 + u2 * dy);
        return new int[] { nx0, ny0, nx1, ny1 };
    }

    public static void drawLine(Graphics2D g, int x0, int y0, int x1, int y1, int h) {
        g.drawLine(x0, y0, x1, y1);
    }

    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("LiangBarsky");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Agregar el Mouse Listener
        LineClipping evento = new LineClipping();
        frame.add(evento);
        frame.addMouseListener(evento);
        // Agregar un JPanel que se llama Points (esta clase)
        frame.add(new LineClipping());
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
}
