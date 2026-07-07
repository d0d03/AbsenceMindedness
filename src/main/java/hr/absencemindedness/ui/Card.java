package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;

import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {

    public Card(int width, int height){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(AppColors.HEADER_BG);
        setForeground(AppColors.HEADER_FG);

        g2.fillRoundRect(0,0,getWidth(), getHeight(), 8,8);
        g2.dispose();
        super.paintComponent(g);
    }
}