package hr.absencemindedness.ui;

import hr.absencemindedness.constants.AppColors;
import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;
import hr.absencemindedness.repositories.CalendarRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class DayCell  extends JPanel {

    private final CalendarKey key;
    private final boolean isNonWorking;
    private boolean hovered = false;
    private final Map<CalendarKey, DayStatus> calendarData;

    public DayCell(Supplier<DayStatus> statusSupplier, Map<CalendarKey, DayStatus> calendarData, CalendarKey key){
        this.key = key;
        this.calendarData = calendarData;
        this.isNonWorking = key.isWeekend();
        setPreferredSize(new Dimension(22,26));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(key.format(DateTimeFormatter.ofPattern("d. MMMM",Locale.getDefault())));
        
        if(!isNonWorking){
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){

                    if(statusSupplier.get() == DayStatus.NONE){
                        if(CalendarRepository.deleteEntry(key)){
                            calendarData.remove(key);
                        }
                    } else {
                        if(CalendarRepository.saveEntry(key, statusSupplier.get())){
                            calendarData.put(key, statusSupplier.get());
                        }
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

        Color base;
        if(isNonWorking){
            base = DayStatus.NON_WORKING.getColor();
        }else{
            DayStatus status  = getStatus();
            base = status == DayStatus.NONE ? AppColors.WHITE : status.getColor();
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
       return calendarData.getOrDefault(key, DayStatus.NONE);
    }

}
