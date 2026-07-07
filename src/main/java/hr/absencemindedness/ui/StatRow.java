package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatRow extends JPanel {

    public StatRow(String label, long value){
        super(new BorderLayout());
        setBorder(new EmptyBorder(0,18,0,18));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 23));
        setBackground(AppColors.HEADER_BG);

        JLabel lb = new JLabel(label);
        lb.setFont(AppFonts.HEADERS);
        lb.setForeground(AppColors.HEADER_FG);

        JLabel val = new JLabel(String.valueOf(value));
        val.setFont(AppFonts.HEADERS);
        val.setForeground(AppColors.HEADER_FG);
        add(lb, BorderLayout.WEST);
        add(val, BorderLayout.EAST);
    }
}
