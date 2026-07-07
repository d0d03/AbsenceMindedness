package hr.absencemindedness.ui;

import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.services.CalendarService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.IntSupplier;

public class StatsPanel extends JPanel {

    private static final long TOTAL_ANNUAL_LEAVE = 30;

    private final IntSupplier yearSupplier;
    private final CalendarService calendarService;
    private final Card card = new Card(150,90);

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
        card.add(Box.createVerticalGlue());
        card.add(new StatRow("Total: ", TOTAL_ANNUAL_LEAVE));
        card.add(new StatRow("Used: ", used));
        card.add(new StatRow("Remaining: ", TOTAL_ANNUAL_LEAVE - used));
        card.add(Box.createVerticalGlue());
    }

    private long getUsedLeaveForYear(int year){
        return calendarService.allEntries().stream()
              .filter(entry ->
                      (entry.getKey().year() == year && (entry.getValue()).equals(DayStatus.USED)))
              .count();
    }
}
