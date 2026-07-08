package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;
import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;
import hr.absencemindedness.models.Holiday;
import hr.absencemindedness.services.CalendarService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class CalendarGridPanel extends JPanel {

    private final JPanel gridPanel;
    private final IntSupplier yearSupplier;
    private final Supplier<DayStatus> statusSupplier;
    private final Supplier<Map<LocalDate, Holiday>> holidaySupplier;
    private final CalendarService calendarService;

    public CalendarGridPanel(IntSupplier yearSupplier, Supplier<DayStatus> statusSupplier, Supplier<Map<LocalDate, Holiday>> holidaysSupplier, CalendarService calendarService){
        super(new BorderLayout());
        setBackground(AppColors.GRID_BG);

        this.yearSupplier = yearSupplier;
        this.statusSupplier = statusSupplier;
        this.holidaySupplier = holidaysSupplier;
        this.calendarService = calendarService;

        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(AppColors.GRID_BG);
        gridPanel.setBorder(new EmptyBorder(12,14,4,14));

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppColors.GRID_BG);

        add(scroll, BorderLayout.CENTER);
        buildGridContent(yearSupplier.getAsInt());
    }

    public void rebuildGrid(int year){
        gridPanel.removeAll();
        buildGridContent(year);
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void buildGridContent(int year){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1,1,1,1);

        addDayNumbersRow(gbc);
        addMonthRows(gbc, year);
    }

    private void addDayNumbersRow(GridBagConstraints gbc){
        //Headers row: day numbers
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel empty = new JLabel("");
        empty.setPreferredSize(new Dimension(90,22));
        gridPanel.add(empty, gbc);

        for(int d = 1; d <= 31; d++){
            gbc.gridx = d;
            gbc.weightx = 1.0;
            JLabel dayLbl = new JLabel(String.valueOf(d), SwingConstants.CENTER);
            dayLbl.setFont(AppFonts.HEADERS);
            dayLbl.setForeground(AppColors.GRID_FG);
            dayLbl.setPreferredSize(new Dimension(22,22));
            gridPanel.add(dayLbl, gbc);
        }
    }

    private void addMonthRows(GridBagConstraints gbc, int year){

        for(int m = 1; m <= 12; m++){
            gbc.gridy = m ;
            gbc.gridx = 0;
            gbc.weightx = 0;

            JLabel monthLbl = new JLabel(Month.of(m).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
            monthLbl.setFont(AppFonts.HEADERS);
            monthLbl.setForeground(AppColors.GRID_FG);
            monthLbl.setBorder(new EmptyBorder(0,0,0,0));
            monthLbl.setPreferredSize(new Dimension(90,26));
            gridPanel.add(monthLbl, gbc);

            int daysInMonth = Month.of(m).length(Year.isLeap(year));
            for(int d = 1; d <= 31; d++){
                gbc.gridx = d;
                gbc.weightx = 1.0;

                if(d <= daysInMonth) {
                    DayCell cell = new DayCell(statusSupplier, holidaySupplier, calendarService, new CalendarKey(year, m, d));
                    gridPanel.add(cell, gbc);
                }
            }
        }
    }

}
