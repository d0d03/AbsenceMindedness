package hr.absencemindedness.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record CalendarKey(int year, int month, int day) {

    public LocalDate toLocalDate(){
        return LocalDate.of(year, month, day);
    }

    public boolean isWeekend(){
        DayOfWeek dow = toLocalDate().getDayOfWeek();
        return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
    }

    public String format(DateTimeFormatter formatter){
        return toLocalDate().format(formatter);
    }
}
