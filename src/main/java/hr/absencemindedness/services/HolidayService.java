package hr.absencemindedness.services;

import hr.absencemindedness.client.HolidayApiClient;
import hr.absencemindedness.models.Holiday;
import hr.absencemindedness.repositories.HolidayRepository;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public final class HolidayService {

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private HolidayService() {
        throw new IllegalStateException("Utility class");
    }

    public static void loadHolidays(int year, String countryCode, Consumer<List<Holiday>> onLoaded){
        if(HolidayRepository.existsForYearAndCountry(year, countryCode)){
            onLoaded.accept(HolidayRepository.loadByYearAndCountry(year, countryCode));
            return;
        }

        new SwingWorker<List<Holiday>, Void>(){
            @Override
            protected List<Holiday> doInBackground() throws IOException, InterruptedException{
                IOException lastIOError = null;
                for(int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++){
                    try {
                        List<Holiday> holidays = HolidayApiClient.fetchHolidays(year, countryCode);
                        HolidayRepository.saveAll(holidays, countryCode);
                        return holidays;
                    } catch (IOException e) {
                        lastIOError = e;
                    }
                }
                throw lastIOError;
            }

            @Override
            protected void done(){
                try{
                    onLoaded.accept(get());
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    onLoaded.accept(List.of());
                } catch (ExecutionException e){
                    System.err.println("Failed to fetch holidays: " + e.getCause().getMessage());
                    onLoaded.accept(List.of());
                }
            }
        }.execute();
    }
}
