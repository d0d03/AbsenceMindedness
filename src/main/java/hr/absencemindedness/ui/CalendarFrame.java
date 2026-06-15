package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.constants.AppFonts;
import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;
import hr.absencemindedness.repositories.CalendarRepository;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

public class CalendarFrame extends JFrame {

    private static final String MAIN_TITLE = "AbsenceMindedness";
    private static final String LOCALE = null;

    private final JPanel gridPanel = new JPanel();
    private final Map<CalendarKey, DayStatus> calendarData = CalendarRepository.loadAll();

    private DayStatus selectedStatus = DayStatus.PLANED;
    private JLabel statusLabel;
    private int currentYear = LocalDate.now().getYear();

    public CalendarFrame(){
        super(MAIN_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));
        getContentPane().setBackground(AppColors.GRID_BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildGrid(), BorderLayout.CENTER);
        add(buildLegend(), BorderLayout.SOUTH);

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

    private JPanel buildHeader(){
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppColors.HEADER_BG);
        header.setBorder(new EmptyBorder(12,18,12,18));

        JPanel titlePanel = buildTitlePanel();

        statusLabel = new JLabel("Active tool: " + selectedStatus.getLabel());
        statusLabel.setFont(AppFonts.BODY);
        statusLabel.setForeground(AppColors.HEADER_FG);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel(MAIN_TITLE);
        title.setFont(AppFonts.TITLE);
        title.setForeground(Color.WHITE);

        YearSpinner yearSpinner = new YearSpinner();
        yearSpinner.addChangeListener(e-> {
            currentYear = yearSpinner.getYear();
            rebuildGrid(currentYear);
        });

        titlePanel.add(title);
        titlePanel.add(yearSpinner);
        return titlePanel;
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

        Locale locale = CalendarFrame.LOCALE == null || CalendarFrame.LOCALE.isBlank() ? Locale.getDefault() : Locale.of(CalendarFrame.LOCALE);
        for(int m = 1; m <= 12; m++){
            gbc.gridy = m ;
            gbc.gridx = 0;
            gbc.weightx = 0;

            JLabel monthLbl = new JLabel(Month.of(m).getDisplayName(TextStyle.FULL_STANDALONE, locale));
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
                    DayCell cell = new DayCell(this, new CalendarKey(year, m, d), LOCALE);
                    gridPanel.add(cell, gbc);
                }
            }
        }
    }

    private JPanel buildLegend(){
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(AppColors.FOOTER_BG);
        outer.setBorder(new CompoundBorder(
                new MatteBorder(1,0,0,0, AppColors.FOOTER_BRD),
                new EmptyBorder(10,18,12,18)
        ));

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 14,2));
        legend.setBackground(AppColors.FOOTER_BG);

        JLabel legendTitle = new JLabel("Chose status type: ");
        legendTitle.setFont(AppFonts.HEADERS);
        legendTitle.setForeground(AppColors.GRID_FG);
        legend.add(legendTitle);

        ButtonGroup bg = new ButtonGroup();
        for(DayStatus status : DayStatus.values()){
            JToggleButton btn = createLegendButton(status);
            bg.add(btn);
            legend.add(btn);
            btn.setSelected(status==selectedStatus);
            btn.addActionListener(e -> {
                selectedStatus = status;
                statusLabel.setText("Active tool: " + status.getLabel());
            });
        }
        outer.add(legend, BorderLayout.CENTER);
        return outer;
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
