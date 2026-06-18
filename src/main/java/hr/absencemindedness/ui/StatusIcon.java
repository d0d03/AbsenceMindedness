package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;

import javax.swing.*;
import java.awt.*;

public class StatusIcon implements Icon{

        private final Color color;

        public StatusIcon(Color color){
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(x, y + 1, 12,12,4,4);
            g2.setColor(AppColors.ICON_BRD);
            g2.drawRoundRect(x, y+1, 12,12,4,4);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 14;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }
}
