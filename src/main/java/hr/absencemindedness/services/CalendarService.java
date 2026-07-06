package hr.absencemindedness.services;

import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;
import hr.absencemindedness.repositories.CalendarRepository;
import hr.absencemindedness.state.CalendarStore;

import java.util.Map;
import java.util.Set;

public class CalendarService {

    //TODO:see about making this and calendarStore singletons
    private final CalendarStore calendarStore;

    public CalendarService(){
        this.calendarStore = new CalendarStore(CalendarRepository.loadAll());
    }

    public Map<CalendarKey, DayStatus> loadAllCalendarEntries() {
        return CalendarRepository.loadAll();
    }

    public void saveStatus(CalendarKey key, DayStatus status){
        if(!calendarStore.entries().contains(Map.entry(key,status)) && CalendarRepository.saveEntry(key, status)){
            calendarStore.put(key, status);
        }
    }

    public void clearStatus(CalendarKey key){
        if(!calendarStore.get(key).equals(DayStatus.NONE) && CalendarRepository.deleteEntry(key)){
            calendarStore.remove(key);
        }
    }

    public DayStatus getStatus(CalendarKey key){
       return calendarStore.get(key);
    }

    public Set<Map.Entry<CalendarKey, DayStatus>> allEntries() {
        return calendarStore.entries();
    }

    public void addListener(Runnable listener){
        calendarStore.addListener(listener);
    }


}
