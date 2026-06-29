package hr.absencemindedness.services;

import hr.absencemindedness.client.HolidayApiClient;
import hr.absencemindedness.models.Holiday;
import hr.absencemindedness.repositories.HolidayRepository;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
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
            protected List<Holiday> doInBackground() throws Exception{
                List<Throwable> suppressedErrors = new ArrayList<>();
                for(int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++){
                    try {
                        List<Holiday> holidays = HolidayApiClient.fetchHolidays(year, countryCode);
                        HolidayRepository.saveAll(holidays, countryCode);
                        return holidays;
                    } catch (IOException e) {
                        suppressedErrors.add(e);

                        System.err.printf("[HolidayService] Attempt %d/%d failed. Cause: %s%n", (attempt + 1), MAX_RETRY_ATTEMPTS, e.getMessage());

                        Thread.sleep(500L * (attempt + 1));
                    }
                }
                IOException finalException = new IOException("Failed to fetch holidays after " + MAX_RETRY_ATTEMPTS + " attempts");
                suppressedErrors.forEach(finalException::addSuppressed);
                throw finalException;
            }

            @Override
            protected void done(){
                try{
                    onLoaded.accept(get());
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    onLoaded.accept(List.of());
                } catch (ExecutionException e){
                    System.err.println(e.getMessage());
                    onLoaded.accept(List.of());
                }
            }
        }.execute();
    }
}
