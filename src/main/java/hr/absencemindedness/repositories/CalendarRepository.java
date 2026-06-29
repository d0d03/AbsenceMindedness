package hr.absencemindedness.repositories;

import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class CalendarRepository {

    private CalendarRepository(){
        throw new IllegalStateException("Utility class");
    }

    public static Map<CalendarKey, DayStatus> loadAll()  {
        Map<CalendarKey, DayStatus> data = new HashMap<>();
        String sql = """
                SELECT ce.*
                FROM calendar_entries ce
                """;

        try(PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    CalendarKey key = new CalendarKey(rs.getInt("year"), rs.getInt("month"), rs.getInt("day"));
                    data.put(key, DayStatus.valueOf(rs.getString("status")));
                }
        } catch(SQLException e){
            System.err.println("Failed to load calendar data: " + e.getMessage());
        }
        return data;
    }

    public static boolean saveEntry(CalendarKey key, DayStatus status){
        String sql = """
                MERGE INTO calendar_entries ("year", "month", "day", "status")
                KEY ("year", "month", "day")
                VALUES (?,?,?,?)
                """;
        try(PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setInt(1, key.year());
            ps.setInt(2,key.month());
            ps.setInt(3,key.day());
            ps.setString(4, status.name());
            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            System.err.println("Failed to save entry " + key + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteEntry(CalendarKey key){
        String sql = """
                DELETE FROM calendar_entries
                WHERE "year" = ? AND "month" = ? AND "day" = ?
                """;
        try(PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setInt(1,key.year());
            ps.setInt(2,key.month());
            ps.setInt(3,key.day());
            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            System.err.println("Failed to delete entry " + key + ": " + e.getMessage());
            return false;
        }
    }

}
