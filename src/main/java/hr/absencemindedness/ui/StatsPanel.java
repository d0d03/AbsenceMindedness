package hr.absencemindedness.ui;

import hr.absencemindedness.helpers.StatsHelper;
import hr.absencemindedness.models.Stats;
import hr.absencemindedness.services.CalendarService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.IntSupplier;

public class StatsPanel extends JPanel {

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

    public void rebuildStatsContent(){
        card.removeAll();
        buildStatsContent();
        card.revalidate();
        card.repaint();
    }

    private void buildStatsContent(){
        Stats stats = StatsHelper.calculateForYear(calendarService, yearSupplier.getAsInt());

        card.add(Box.createVerticalGlue());
        card.add(new StatRow("Total: ", StatsHelper.TOTAL_ANNUAL_LEAVE));
        card.add(new StatRow("Planned: ", stats.planned()));
        card.add(new StatRow("Approved: ", stats.approved()));
        card.add(new StatRow("Used: ", stats.used()));
        card.add(new StatRow("Remaining: ", stats.remaining()));
        card.add(new StatRow("Home-office (monthly): ", stats.homeOfficeRemainingThisMonth()));
        card.add(Box.createVerticalGlue());
    }
}
