package hr.absencemindedness.ui;

import hr.absencemindedness.repositories.DatabaseManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class AppWindowListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e){
        try {
            DatabaseManager.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
