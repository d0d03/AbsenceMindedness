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
            JToggleButton btn = new LegendButton(status);
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
}
