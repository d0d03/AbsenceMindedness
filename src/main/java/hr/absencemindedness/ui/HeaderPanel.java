package hr.absencemindedness.ui;

import hr.absencemindedness.config.AppConfig;
import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;
import hr.absencemindedness.enums.DayStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.IntConsumer;

public class HeaderPanel extends JPanel {

    private static final String ACTIVE_TOOL = "Active tool: ";
    private final JLabel statusLabel;

    public HeaderPanel(String mainTitle, DayStatus selectedStatus, IntConsumer onYearChange){
        super(new BorderLayout());
        setBackground(AppColors.HEADER_BG);
        setBorder(new EmptyBorder(12,18,12,18));

        JPanel titlePanel = buildTitlePanel(mainTitle, onYearChange);
        statusLabel = new JLabel(ACTIVE_TOOL.concat(selectedStatus.getLabel()));
        statusLabel.setFont(AppFonts.BODY);
        statusLabel.setForeground(AppColors.HEADER_FG);

        add(titlePanel, BorderLayout.WEST);
        add(statusLabel, BorderLayout.EAST);
    }

    private JPanel buildTitlePanel(String mainTitle, IntConsumer onYearChange) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel(mainTitle);
        title.setFont(AppFonts.TITLE);
        title.setForeground(Color.WHITE);
        title.setToolTipText("v" + AppConfig.get("app.version", "dev"));

        YearSpinner yearSpinner = new YearSpinner();
        yearSpinner.addChangeListener( e -> onYearChange.accept(yearSpinner.getYear()));

        titlePanel.add(title);
        titlePanel.add(yearSpinner);
        return titlePanel;
    }

    public void updateStatusLabel(String text){
        statusLabel.setText(ACTIVE_TOOL.concat(text));
    }
}
