/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Controlador.Conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author PC
 */
public class GraficaBarras extends javax.swing.JFrame {
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public GraficaBarras() {
        initComponents();
        Conexion cn = new Conexion();

        // Crea un conjunto de datos para la gráfica de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Conexión a la base de datos (ajusta la URL, usuario y contraseña)
        try {
            con = cn.getConnection();

            // Consulta SQL para obtener datos de entrada_materia_prima
            String sql = "SELECT nombre_materia, cantidad FROM entrada_materia_prima";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String nombreMateria = rs.getString("nombre_materia");
                int cantidad = rs.getInt("cantidad");
                dataset.addValue(cantidad, "Entradas", nombreMateria);
            }

            // Cierra los recursos
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Crea la gráfica de barras
        JFreeChart chart = ChartFactory.createBarChart(
                "Gráfica de Barras de Entradas de Materia Prima",
                "Materia Prima", // Etiqueta del eje X
                "Cantidad", // Etiqueta del eje Y
                dataset, // Conjunto de datos
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Personaliza la apariencia de la gráfica si es necesario
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        // Crea un panel de gráfica y agrégalo a tu formulario
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
        jPanel18.add(chartPanel);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new GraficaBarras().setVisible(true);
        });
    }

    private javax.swing.JPanel jPanel18;

    private void initComponents() {
        jPanel18 = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel18.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel18, java.awt.BorderLayout.CENTER);
        pack();
    }
}