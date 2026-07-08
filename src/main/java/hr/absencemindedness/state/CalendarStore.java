package hr.absencemindedness.state;

import hr.absencemindedness.enums.DayStatus;
import hr.absencemindedness.models.CalendarKey;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CalendarStore {

    private final Map<CalendarKey, DayStatus> calendarData;
    private final List<Runnable> listeners;

    public CalendarStore(Map<CalendarKey, DayStatus> calendarData){
        this.calendarData = calendarData;
        this.listeners = new ArrayList<>();
    }

    public void put(CalendarKey key, DayStatus status) {
        calendarData.put(key, status);
        notifyAllListeners();
    }

    public void remove(CalendarKey key) {
        calendarData.remove(key);
        notifyAllListeners();
    }

    public DayStatus get(CalendarKey key){
        return calendarData.getOrDefault(key, DayStatus.NONE);
    }

    public void forEachMatching(Predicate<Map.Entry<CalendarKey, DayStatus>> predicate, Consumer<Map.Entry<CalendarKey, DayStatus>> action){
        for(Map.Entry<CalendarKey, DayStatus> e : calendarData.entrySet()){
            if(predicate.test(e)){
                action.accept(e);
            }
        }
    }

    public void addListener(Runnable listener){
        listeners.add(listener);
    }

    private void notifyAllListeners(){
        for(Runnable listener : listeners){
            listener.run();
        }
    }
}
