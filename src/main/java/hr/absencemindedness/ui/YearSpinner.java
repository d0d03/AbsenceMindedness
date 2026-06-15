package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class YearSpinner extends JPanel {

    private final JLabel yearLabel;
    private final List<ChangeListener> listeners = new ArrayList<>();
    private int year = LocalDate.now().getYear();

    public YearSpinner(){
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        setOpaque(false);

        JButton prev = createArrowButton("<");
        JButton next = createArrowButton(">");

        yearLabel = new JLabel(String.valueOf(year));
        yearLabel.setFont(AppFonts.TITLE);
        yearLabel.setForeground(AppColors.WHITE);


        prev.addActionListener(e -> updateYear(year -1 ));
        next.addActionListener(e -> updateYear(year + 1));

        add(prev);
        add(yearLabel);
        add(next);
    }

    public int getYear(){
        return year;
    }

    public void addChangeListener(ChangeListener l){
        listeners.add(l);
    }

    private JButton createArrowButton(String text){
        JButton btn = new JButton(text);
        btn.setFont(AppFonts.BODY);
        btn.setForeground(AppColors.HEADER_BG);
        btn.setBackground(AppColors.HEADER_BG);
        btn.setBorder(BorderFactory.createEmptyBorder(2,6,2,6));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateYear(int newYear){
        if(newYear < 2000 || newYear > 2100) return;
        year = newYear;
        yearLabel.setText(String.valueOf(year));
        listeners.forEach(l -> l.stateChanged(new ChangeEvent(this)));
    }

}
