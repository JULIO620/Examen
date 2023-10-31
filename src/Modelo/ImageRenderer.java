/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author user
 */
public class ImageRenderer extends DefaultTableCellRenderer {
      @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);

        // Ajusta el tamaño de la imagen para que quepa en la celda
        ImageIcon icon = (ImageIcon) value;
        if (icon != null) {
            int width = table.getColumnModel().getColumn(column).getWidth();
            int height = table.getRowHeight(row);
            icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
            label.setIcon(icon);
        } else {
            label.setIcon(null);  // Si no hay imagen, establece el icono en null
        }

        return label;
    }
}

