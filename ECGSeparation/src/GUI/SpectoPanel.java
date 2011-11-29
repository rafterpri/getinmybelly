package GUI;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Tom Pepels
 */
public class SpectoPanel extends javax.swing.JPanel {

    ChartPanel chartPanel;
    JFreeChart chart;
    XYPlot xyPlot;
    int datasets = 1;

    /** Creates new form GraphPanel */
    public SpectoPanel() {
        initComponents();
    }

    public void drawSpecto(double[][] data, int overlap, int window) {
        this.removeAll();

        XYSeries series = new XYSeries("Spectogram");
        // Add the first data, since it has no overlap.
        for (int i = 0; i < data[0].length; i++) {
            series.add(i, data[0][i]);
        }

        for (int j = 1; j < data.length; j++) {
            int offset = j*(window-overlap);
            System.out.println("[" + j + "] Start at: " + offset + " end at: " + (offset + window));
            for (int i = 0; i < window; i++) {
                series.add(offset + i, data[j][i]);
            }
        }

        XYDataset plotData = new XYSeriesCollection(series);

        chart = ChartFactory.createScatterPlot("", "", "", plotData, 
                PlotOrientation.VERTICAL, true, true, true);
        XYDotRenderer renderer = new XYDotRenderer();
        xyPlot = chart.getXYPlot();
        xyPlot.setRenderer(renderer);
        xyPlot.setBackgroundPaint(Color.black);
        xyPlot.getRangeAxis().setAutoRange(true);

        chartPanel = new ChartPanel(chart);
        chartPanel.setSize(this.getWidth(), this.getHeight());
        chartPanel.setLocation(0, 0);
        add(chartPanel);
        repaint();
        revalidate();
    }
    /*
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
    
    xWidth = this.getWidth() / (data.length * data[0].length); // Size of a single value on y axis
    yHeight = (int) (this.getHeight() / (max - min));    // Size of a single value on y axis
    
    double maxSig = Math.max(Math.abs(max), Math.abs(min));
    crange = 255 / (maxSig);
    }
    
    @Override
    public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (data != null) {
    int h = this.getHeight();
    g.setColor(Color.white);
    // Find the max value
    for (int i = 0; i < data.length; i++) {
    for (int j = 0; j < data[i].length; j++) {
    double point = (data[i][j] * yHeight) + yHeight;
    g.fillRect(i * xWidth, h - (int)point, xWidth, yHeight);
    }
    }
    }
    }
     * */

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
        if (chartPanel != null) {
            chartPanel.setSize(this.getWidth(), this.getHeight());
        }
    }//GEN-LAST:event_formComponentResized
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
