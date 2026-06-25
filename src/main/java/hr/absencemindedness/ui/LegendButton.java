package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;
import hr.absencemindedness.enums.DayStatus;

import javax.swing.*;
import java.awt.*;

public class LegendButton extends JToggleButton{

    public LegendButton(DayStatus status){
        super(status.getLabel());
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(AppFonts.CELL);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(4,8,4,8));
        setIcon(new StatusIcon(status.getColor()));
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(isSelected()){
            g2.setColor(AppColors.GRID_FG);
            setForeground(AppColors.WHITE);
        }else if(getModel().isRollover()){
            g2.setColor(AppColors.ICON_HVR);
            setForeground(AppColors.HEADER_BG);
        }else{
            g2.setColor(AppColors.FOOTER_BG);
            setForeground(AppColors.GRID_FG);
        }
        g2.fillRoundRect(0,0,getWidth(), getHeight(), 8,8);
        g2.dispose();
        super.paintComponent(g);
    }
}
