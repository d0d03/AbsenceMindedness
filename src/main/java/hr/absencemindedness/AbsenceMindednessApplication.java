package hr.absencemindedness;

import hr.absencemindedness.repositories.DatabaseManager;
import hr.absencemindedness.ui.CalendarFrame;

import javax.swing.*;
import java.sql.SQLException;

public class AbsenceMindednessApplication {

    public static void main(String[] args){

        try {
            DatabaseManager.initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignoring) {
            //Empty because I still don't know for what to watch out and handle
        }

        SwingUtilities.invokeLater(CalendarFrame::new);
    }
}
