package hr.absencemindedness;

import hr.absencemindedness.ui.CalendarFrame;

import javax.swing.*;

public class AbsenceMindednessApplication {

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignoring) {
            //Empty because I still don't know for what to watch out and handle
        }

        SwingUtilities.invokeLater(CalendarFrame::new);
    }
}
