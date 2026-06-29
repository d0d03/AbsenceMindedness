package hr.absencemindedness.ui;

import hr.absencemindedness.config.AppConfig;
import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;
import hr.absencemindedness.models.Holiday;
import hr.absencemindedness.repositories.CalendarRepository;
import hr.absencemindedness.services.HolidayService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class CalendarFrame extends JFrame {

    private static final String MAIN_TITLE = "AbsenceMindedness";
    private final Map<CalendarKey, DayStatus> calendarData = CalendarRepository.loadAll();
    private final CalendarGridPanel grid;
    private Map<LocalDate, Holiday> holidays = Map.of();

    private DayStatus selectedStatus = DayStatus.PLANED;
    private int currentYear = LocalDate.now().getYear();

    public CalendarFrame(){
        super(MAIN_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));
        getContentPane().setBackground(AppColors.GRID_BG);

        grid = new CalendarGridPanel(() -> currentYear, () -> selectedStatus, () -> holidays, calendarData);
        loadHolidaysForYear(currentYear);

        HeaderPanel header =  new HeaderPanel(MAIN_TITLE, selectedStatus, year -> {
            currentYear = year;
            loadHolidaysForYear(currentYear);
        });

        LegendPanel legend = new LegendPanel(selectedStatus, status -> {
            selectedStatus = status;
            header.updateStatusLabel(selectedStatus.getLabel());
        });

        add(header, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(legend, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(900, 480));
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new AppWindowListener());

    }

    private void loadHolidaysForYear(int year){
        String countryCode = AppConfig.get("app.locale", "HR");
        HolidayService.loadHolidays(year, countryCode.toUpperCase(), list -> holidays = list.stream().collect(Collectors.toMap(Holiday::date, h-> h)));
        grid.rebuildGrid(currentYear);
    }

}
