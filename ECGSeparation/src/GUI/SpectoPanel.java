package GUI;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Tom Pepels
 */
public class SpectoPanel extends javax.swing.JPanel {

    double[][] data;
    double max, min, crange;
    int yHeight, xWidth;

    /** Creates new form GraphPanel */
    public SpectoPanel() {
        initComponents();
    }

    public void drawSpecto(double[][] data) {
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
        this.data = data;
        // Find the max value
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] > max) {
                    max = data[i][j];
                }
                if (data[i][j] < min) {
                    min = data[i][j];
                }
            }
        }

        xWidth = this.getWidth() / data.length; // Size of a single value on y axis
        yHeight = this.getHeight() / data[0].length;    // Size of a single value on y axis

        double maxSig = Math.max(Math.abs(max), Math.abs(min));
        crange = 255 / (maxSig);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data != null) {
            // Find the max value
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    double val = data[i][j];
                    double r = Math.abs(val) * crange;
                    Color col = new Color(255, (int) r, 0);
                    g.setColor(col);
                    g.fillRect(i * xWidth, (j * yHeight) + yHeight, xWidth, yHeight);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(0, 0, 0));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        drawSpecto(data);
    }//GEN-LAST:event_formComponentResized
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
