package hr.absencemindedness.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import hr.absencemindedness.models.Holiday;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public final class HolidayApiClient {

    private static final String BASE_URL = "https://date.nager.at/api/v3/PublicHolidays";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                    LocalDate.parse(json.getAsString()))
            .create();

    private HolidayApiClient(){
        throw new IllegalStateException("Utility class");
    }

    public static List<Holiday> fetchHolidays(int year, String countryCode) throws IOException, InterruptedException {
        String url = BASE_URL + "/" + year + "/" + countryCode;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200){
            throw new IOException("Failed to fetch holidays: HTTP " + response.statusCode());
        }

        Type listType  = new TypeToken<List<Holiday>>(){}.getType();

        return gson.fromJson(response.body(), listType);
    }

}
