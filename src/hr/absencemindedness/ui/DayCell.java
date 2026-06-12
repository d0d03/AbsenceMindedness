package hr.absencemindedness.ui;

import hr.absencemindedness.enums.DayStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class DayCell  extends JPanel {

    private final int month;
    private final int day;
    private boolean hovered = false;
    private final CalendarFrame frame;

    public DayCell(CalendarFrame frame, int month, int day, String lang){
        Locale locale = lang == null || lang.isBlank() ? Locale.getDefault() : Locale.of(lang);
        this.month = month;
        this.day = day;
        this.frame = frame;
        setPreferredSize(new Dimension(22,26));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(MonthDay.of(month + 1, day).format(DateTimeFormatter.ofPattern("d. MMMM",locale)));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                Map<Integer, DayStatus> monthData = frame.getCalendarData().get(month);
                if(frame.getSelectedStatus() == DayStatus.NONE){
                    monthData.remove(day);
                } else {
                    monthData.put(day, frame.getSelectedStatus());
                }
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }

            @Override
            public void mouseExited(MouseEvent e) { hovered = false; repaint(); }

        });
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        DayStatus status  = getStatus();
        Color base = status == DayStatus.NONE ? new Color(252, 252, 255) : status.getColor();

        //Fill
        g2.setColor(hovered ? base.darker() : base);
        g2.fillRect(0,0,getWidth(), getHeight());

        //Border
        if(hovered){
            g2.setColor(new Color(50,60,80));
            g2.setStroke(new BasicStroke(1.5f));
        } else {
            g2.setColor(new Color(200,200,210));
            g2.setStroke(new BasicStroke(1f));
        }
        g2.drawRect(0,0,getWidth() - 1, getHeight() - 1);

        g2.dispose();
    }

    private DayStatus getStatus() {
       return frame.getCalendarData().getOrDefault(month, Collections.emptyMap())
                .getOrDefault(day, DayStatus.NONE);
    }

}
