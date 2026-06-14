package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DayCell  extends JPanel {

    private final int year;
    private final int month;
    private final int day;
    private final CalendarFrame frame;
    private final boolean isNonWorking;
    private boolean hovered = false;

    public DayCell(CalendarFrame frame, LocalDate selectedDate, String lang){
        Locale locale = lang == null || lang.isBlank() ? Locale.getDefault() : Locale.of(lang);
        this.month = selectedDate.getMonthValue();
        this.day = selectedDate.getDayOfMonth();
        this.year = selectedDate.getYear();
        this.frame = frame;
        this.isNonWorking = isWeekend(selectedDate);
        setPreferredSize(new Dimension(22,26));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(selectedDate.format(DateTimeFormatter.ofPattern("d. MMMM",locale)));
        
        if(!isNonWorking){
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    if(frame.getSelectedStatus() == DayStatus.NONE){
                        frame.getCalendarData().remove(new CalendarKey(year, month, day));
                    } else {
                        frame.getCalendarData().put(new CalendarKey(year, month, day), frame.getSelectedStatus());
                    }
                    repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }

                @Override
                public void mouseExited(MouseEvent e) { hovered = false; repaint(); }

            });
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        DayStatus status  = getStatus();
        Color base = status == DayStatus.NONE ? AppColors.WHITE : status.getColor();

        if(isNonWorking){
            base = DayStatus.NON_WORKING.getColor();
        }

        //Fill
        g2.setColor(hovered ? base.darker() : base);
        g2.fillRect(0,0,getWidth(), getHeight());

        //Border
        if(hovered){
            g2.setColor(AppColors.GRID_FG);
            g2.setStroke(new BasicStroke(1.5f));
        } else {
            g2.setColor(AppColors.FOOTER_BRD);
            g2.setStroke(new BasicStroke(1f));
        }
        g2.drawRect(0,0,getWidth() - 1, getHeight() - 1);

        g2.dispose();
    }

    private DayStatus getStatus() {
       return frame.getCalendarData().getOrDefault(new CalendarKey(year, month, day), DayStatus.NONE);
    }

    private boolean isWeekend(LocalDate selectedDate){
        return selectedDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || selectedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

}
