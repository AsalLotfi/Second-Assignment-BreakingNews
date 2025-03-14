package AP;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class Infrastructure {

    private final String URL;
    private final String APIKEY;
    private final String JSONRESULT;
    private ArrayList<News> newsList;


    public Infrastructure(String APIKEY) {
        this.APIKEY = APIKEY;
        this.URL = "https://newsapi.org/v2/everything?q=tesla&from=" + LocalDate.now().minusDays(1) + "&sortBy=publishedAt&apiKey=";
        this.JSONRESULT = getInformation();
        parseInformation(); // Call the method to parse the JSON
    }

    public ArrayList<News> getNewsList() {
        return newsList;
    }

    private String getInformation() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + APIKEY))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("HTTP error code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("!!Exception : " + e.getMessage());
        }
        return null;
    }

    private void parseInformation() {

        if (JSONRESULT == null) {
            System.out.println("No JSON Data available");
            return;
        }

        try {

            Gson gson = new Gson();
            NewsResponse response = gson.fromJson(JSONRESULT, NewsResponse.class);

            // Get the first 20 articles (or all if fewer than 20)
            newsList = new ArrayList<>(response.getArticles().subList(0, Math.min(20, response.getArticles().size())));

        } catch(JsonSyntaxException e) {

            System.out.println("Error Parsing Json : " + e.getMessage());
        }
    }

    public void displayNewsList() {

        if (newsList == null || newsList.isEmpty()){
            System.out.println("No News available.");
            return;
        }

        // print News titles
        System.out.println("Latest News:");
        for (int i = 0; i < newsList.size(); i++) {
            System.out.println((i + 1) + ". " + newsList.get(i).getTitle());
        }

        // Let user choose a news article
        System.out.println("Enter a number to view full details (or 0 to exit): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        boolean exit = false;
        while (!exit) {

            if (choice > 0 && choice <= newsList.size()) {
                System.out.println("\n" + newsList.get(choice - 1)); // Print full news details

            } else if (choice == 0) {
                exit = true;
                return;

            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
