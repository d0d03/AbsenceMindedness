package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;
import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;
import hr.absencemindedness.repositories.CalendarRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

public class CalendarFrame extends JFrame {

    private static final String MAIN_TITLE = "AbsenceMindedness";

    private final JPanel gridPanel = new JPanel();
    private final Map<CalendarKey, DayStatus> calendarData = CalendarRepository.loadAll();

    private DayStatus selectedStatus = DayStatus.PLANED;
    private int currentYear = LocalDate.now().getYear();

    public CalendarFrame(){
        super(MAIN_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));
        getContentPane().setBackground(AppColors.GRID_BG);

        HeaderPanel header =  new HeaderPanel(MAIN_TITLE, selectedStatus, year -> {
            currentYear = year;
            rebuildGrid(currentYear);
        });
        add(header, BorderLayout.NORTH);

        add(buildGrid(), BorderLayout.CENTER);

        LegendPanel legend = new LegendPanel(selectedStatus, status -> {
            selectedStatus = status;
            header.updateStatusLabel(selectedStatus.getLabel());
        });
        add(legend, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(900, 480));
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new AppWindowListener());

    }

    protected DayStatus getSelectedStatus(){
        return this.selectedStatus;
    }

    protected Map<CalendarKey, DayStatus> getCalendarData(){
        return this.calendarData;
    }

    protected int getCurrentYear(){
       return currentYear;
    }

    private JScrollPane buildGrid(){
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(AppColors.GRID_BG);
        gridPanel.setBorder(new EmptyBorder(12,14,4,14));

        buildGridContent(getCurrentYear());

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppColors.GRID_BG);
        return scroll;
    }

    private void buildGridContent(int year){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1,1,1,1);

        this.addDayNumbersRow(gbc);
        this.addMonthRows(gbc, year);
    }

    private void rebuildGrid(int year){
        gridPanel.removeAll();
        buildGridContent(year);
        gridPanel.revalidate();
        gridPanel.repaint();
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

            for(int d = 1; d <= 31; d++){
                gbc.gridx = d;
                gbc.weightx = 1.0;

                int daysInMonth = Month.of(m).length(Year.isLeap(year));
                if(d <= daysInMonth) {
                    DayCell cell = new DayCell(this, new CalendarKey(year, m, d));
                    gridPanel.add(cell, gbc);
                }
            }
        }
    }

}
