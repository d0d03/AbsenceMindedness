package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;
import hr.absencemindedness.enums.DayStatus;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.function.Consumer;

public class LegendPanel extends JPanel {

    public LegendPanel(DayStatus initialStatus, Consumer<DayStatus> onSelectedStatusChange){
        super(new BorderLayout());
        setBackground(AppColors.FOOTER_BG);
        setBorder(new CompoundBorder(
                new MatteBorder(1,0,0,0, AppColors.FOOTER_BRD),
                new EmptyBorder(10,18,12,18)
        ));

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 14,2));
        legend.setBackground(AppColors.FOOTER_BG);

        legend.add(buildTitleLabel());

        ButtonGroup bg = new ButtonGroup();
        for(DayStatus status : DayStatus.values()){
            JToggleButton btn = createLegendButton(status);
            bg.add(btn);
            legend.add(btn);
            btn.setSelected(status==initialStatus);
            btn.addActionListener(e -> onSelectedStatusChange.accept(status));
        }

        add(legend, BorderLayout.CENTER);
    }

    private JLabel buildTitleLabel(){
        JLabel legendTitle = new JLabel("Chose status type: ");
        legendTitle.setFont(AppFonts.HEADERS);
        legendTitle.setForeground(AppColors.GRID_FG);
        return legendTitle;
    }

    private JToggleButton createLegendButton(DayStatus status){
        JToggleButton btn = new JToggleButton((status.getLabel())){
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if(isSelected()){
                    g2.setColor(AppColors.GRID_FG);
                    this.setForeground(AppColors.WHITE);
                }else if(getModel().isRollover()){
                    g2.setColor(AppColors.ICON_HVR);
                    setForeground(AppColors.HEADER_BG);
                }else{
                    g2.setColor(AppColors.FOOTER_BG);
                    this.setForeground(AppColors.GRID_FG);
                }
                g2.fillRoundRect(0,0,getWidth(), getHeight(), 8,8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(AppFonts.CELL);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(4,8,4,8));

        //Color switch
        Icon icon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(status.getColor());
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
        };
        btn.setIcon(icon);
        return btn;
    }
}
