package hr.absencemindedness.services;

import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;
import hr.absencemindedness.repositories.CalendarRepository;
import hr.absencemindedness.state.CalendarStore;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CalendarService {

    //TODO:see about making this and calendarStore singletons
    private final CalendarStore calendarStore;

    public CalendarService(){
        this.calendarStore = new CalendarStore(CalendarRepository.loadAll());
    }

    public void saveStatus(CalendarKey key, DayStatus status){
        DayStatus current = calendarStore.get(key);
        if ((current == DayStatus.NONE || current != status) && CalendarRepository.saveEntry(key, status)) {
            calendarStore.put(key, status);
        }
    }

    public void clearStatus(CalendarKey key){
        DayStatus current = calendarStore.get(key);
        if (current != DayStatus.NONE && CalendarRepository.deleteEntry(key)) {
            calendarStore.remove(key);
        }
    }

    public DayStatus getStatus(CalendarKey key){
       return calendarStore.get(key);
    }

    public void forEachMatching(Predicate<Map.Entry<CalendarKey, DayStatus>> predicate, Consumer<Map.Entry<CalendarKey, DayStatus>> action){
        calendarStore.forEachMatching(predicate, action);
    }

    public void addListener(Runnable listener){
        calendarStore.addListener(listener);
    }


}
