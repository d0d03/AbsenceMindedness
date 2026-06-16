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
import java.util.Locale;

public class DayCell  extends JPanel {

    private final CalendarKey key;
    private final CalendarFrame frame;
    private final boolean isNonWorking;
    private boolean hovered = false;

    public DayCell(CalendarFrame frame, CalendarKey key){
        this.key = key;
        this.frame = frame;
        this.isNonWorking = key.isWeekend();
        setPreferredSize(new Dimension(22,26));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(key.format(DateTimeFormatter.ofPattern("d. MMMM",Locale.getDefault())));
        
        if(!isNonWorking){
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){

                    if(frame.getSelectedStatus() == DayStatus.NONE){
                        if(CalendarRepository.deleteEntry(key)){
                            frame.getCalendarData().remove(key);
                        }
                    } else {
                        if(CalendarRepository.saveEntry(key, frame.getSelectedStatus())){
                            frame.getCalendarData().put(key, frame.getSelectedStatus());
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
       return frame.getCalendarData().getOrDefault(key, DayStatus.NONE);
    }

}
