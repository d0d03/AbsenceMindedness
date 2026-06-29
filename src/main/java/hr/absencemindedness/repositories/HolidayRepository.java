package hr.absencemindedness.repositories;

import hr.absencemindedness.models.Holiday;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class HolidayRepository {

    private HolidayRepository(){
        throw new IllegalStateException("Utility class");
    }

    public static List<Holiday> loadByYearAndCountry(int year, String countryCode){
        List<Holiday> holidays = new ArrayList<>();
        String sql = """
                SELECT "year", "month", "day", "local_name", "name"
                FROM holidays
                WHERE "year" = ? and "country_code" = ?
                """;
        try(PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setInt(1,year);
            ps.setString(2, countryCode);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    LocalDate date = LocalDate.of(rs.getInt("year"), rs.getInt("month"), rs.getInt("day"));
                    holidays.add(new Holiday(date, rs.getString("local_name"), rs.getString("name")));
                }
            }

        } catch (SQLException e){
            System.err.println("Failed to load holidays for " + year + "/" + countryCode + ": " + e.getMessage());
        }
        return holidays;
    }

    public static boolean saveAll(List<Holiday> holidays, String countryCode){
        String sql = """
                MERGE INTO holidays ("country_code", "year", "month", "day", "local_name", "name")
                KEY ("country_code", "year", "month", "day")
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setString(1,"HR");
            for(Holiday holiday : holidays){
                ps.setInt(2,  holiday.date().getYear());
                ps.setInt(3, holiday.date().getMonthValue());
                ps.setInt(4, holiday.date().getDayOfMonth());
                ps.setString(5, holiday.localName());
                ps.setString(6, holiday.name());
                ps.addBatch();
            }

            ps.executeBatch();
            return true;
        } catch (SQLException e){
            System.err.println("Failed to save holidays for " + countryCode + ": " + e.getMessage() );
            return false;
        }
    }

    public static boolean existsForYearAndCountry(int year, String countryCode){
        String sql = """
                SELECT 1 FROM holidays
                WHERE "year" = ? AND "country_code" = ?
                LIMIT 1;
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setInt(1, year);
            ps.setString(2, countryCode);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }
        } catch (SQLException e){
            System.err.println("Failed to check holidays existence: " + e.getMessage());
            return false;
        }
    }
}
