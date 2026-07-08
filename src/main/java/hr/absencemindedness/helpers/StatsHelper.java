package hr.absencemindedness.helpers;

import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.Stats;
import hr.absencemindedness.services.CalendarService;

import java.time.LocalDate;

public final class StatsHelper {

    public static final long TOTAL_ANNUAL_LEAVE = 30;
    public static final long TOTAL_HOME_OFFICE_PER_MONTH = 13;

    private StatsHelper(){
        throw new IllegalStateException("Utility class");
    }

    public static Stats calculateForYear(CalendarService calendarService, int year){
        class Accumulator {
            long used;
            long planned;
            long approved;
            long homeOfficeRemaining = TOTAL_HOME_OFFICE_PER_MONTH;
        }

        Accumulator acc = new Accumulator();

        calendarService.forEachMatching(
                entry -> entry.getKey().year() == year,
                entry -> {
                    if(entry.getKey().month() == LocalDate.now().getMonthValue() && entry.getValue() == DayStatus.HOME_OFFICE){
                        acc.homeOfficeRemaining--;
                    }
                    switch(entry.getValue()){
                        case USED -> acc.used++;
                        case PLANED -> acc.planned++;
                        case APPROVED -> acc.approved++;
                        default -> {/* other statuses don't count toward these four stats */}
                    }
                }
        );

        long remaining = TOTAL_ANNUAL_LEAVE - acc.used - acc.planned - acc.approved;
        return new Stats(acc.used, acc.planned, acc.approved, remaining ,acc.homeOfficeRemaining);
    }
}
