package hr.absencemindedness.ui;

import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.services.CalendarService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.function.IntSupplier;

public class StatsPanel extends JPanel {

    private static final long TOTAL_ANNUAL_LEAVE = 30;
    private static final long TOTAL_HOME_OFFICE_PER_MONTH = 13;

    private final IntSupplier yearSupplier;
    private final CalendarService calendarService;
    private final Card card = new Card(200,128);

    public StatsPanel(CalendarService calendarService, IntSupplier yearSupplier){
        super(new GridBagLayout());
        this.yearSupplier = yearSupplier;
        this.calendarService = calendarService;
        setOpaque(false);
        setBorder(new EmptyBorder(0,18,0,18));
        calendarService.addListener(this::rebuildStatsContent);
        buildStatsContent();
        add(card, new GridBagConstraints());
    }

    private void rebuildStatsContent(){
        card.removeAll();
        buildStatsContent();
        card.revalidate();
        card.repaint();
    }

    private void buildStatsContent(){
        long used = getUsedLeaveForYear(yearSupplier.getAsInt());
        long planed = getPlanedLeaveForYear(yearSupplier.getAsInt());
        long approved = getApprovedLeaveFroYear(yearSupplier.getAsInt());
        long homeOffice = getHomeOfficeForMonth(yearSupplier.getAsInt(), LocalDate.now().getMonthValue());

        card.add(Box.createVerticalGlue());
        card.add(new StatRow("Total: ", TOTAL_ANNUAL_LEAVE));
        card.add(new StatRow("Planed: ", planed));
        card.add(new StatRow("Approved: ", approved));
        card.add(new StatRow("Used: ", used));
        card.add(new StatRow("Remaining: ", TOTAL_ANNUAL_LEAVE - used - approved - planed));
        card.add(new StatRow("Home-office (monthly): ", TOTAL_HOME_OFFICE_PER_MONTH - homeOffice));
        card.add(Box.createVerticalGlue());
    }

    private long getUsedLeaveForYear(int year){
        return calendarService.allEntries().stream()
              .filter(entry ->
                      (entry.getKey().year() == year && (entry.getValue()).equals(DayStatus.USED)))
              .count();
    }

    private long getPlanedLeaveForYear(int year) {
        return calendarService.allEntries().stream()
                .filter(entry ->
                        entry.getKey().year() == year && (entry.getValue().equals(DayStatus.PLANED)))
                .count();
    }

    private long getHomeOfficeForMonth(int year, int month){
        return calendarService.allEntries().stream()
                .filter(entry ->
                        entry.getKey().year() == year
                        && entry.getKey().month() == month
                        && (entry.getValue().equals(DayStatus.HOME_OFFICE)))
                .count();
    }

    private long getApprovedLeaveFroYear(int year){
        return calendarService.allEntries().stream()
                .filter(entry ->
                        entry.getKey().year() == year
                        && (entry.getValue().equals(DayStatus.APPROVED)))
                .count();
    }
}
