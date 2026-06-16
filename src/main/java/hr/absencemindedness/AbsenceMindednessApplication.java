package hr.absencemindedness;

import hr.absencemindedness.config.AppConfig;
import hr.absencemindedness.repositories.DatabaseManager;
import hr.absencemindedness.ui.CalendarFrame;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Locale;

public class AbsenceMindednessApplication {

    private static final String LOCALE = AppConfig.get("app.locale");

    public static void main(String[] args){

        Locale.setDefault(Locale.of(LOCALE));

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
