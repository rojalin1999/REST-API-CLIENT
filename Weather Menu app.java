import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherMenuApp {

    // Mapping of city names to their latitude and longitude (used by the API)
    static Map<String, String> cityCoords = new HashMap<>();

    // Static block to populate the city-coordinate map
    static {
        cityCoords.put("Cuttack", "20.46,85.88");
        cityCoords.put("Delhi", "28.61,77.21");
        cityCoords.put("Mumbai", "19.07,72.87");
        cityCoords.put("Bengaluru", "12.97,77.59");
        cityCoords.put("Chennai", "13.08,80.27");
        cityCoords.put("Kolkata", "22.57,88.36");
        cityCoords.put("Hyderabad", "17.38,78.48");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            // Display the menu
            System.out.println("\n===== Weather Menu =====");
            System.out.println("Select a city:");
            int i = 1;
            for (String city : cityCoords.keySet()) {
                System.out.println(i++ + ". " + city);  // Display cities with numbered options
            }
            System.out.println(i + ". Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline character

            if (choice >= 1 && choice <= cityCoords.size()) {
                // Get the selected city name
                String city = (String) cityCoords.keySet().toArray()[choice - 1];
                fetchWeather(city);  // Fetch and display weather for selected city
            } else if (choice != cityCoords.size() + 1) {
                System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != cityCoords.size() + 1);  // Continue until user selects Exit

        System.out.println("Exiting program.");
        sc.close();
    }

    // Method to fetch weather data for a selected city using Open-Meteo API
    static void fetchWeather(String city) {
        // Extract latitude and longitude from map
        String coords = cityCoords.get(city);
        String[] parts = coords.split(",");
        String latitude = parts[0];
        String longitude = parts[1];

        // Build the API URL with coordinates
        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                + "&longitude=" + longitude + "&current_weather=true";

        try {
            // Create and open HTTP connection
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Read response from API
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String input;
            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            in.close();
            con.disconnect();

            // Convert response string to JSON object
            JSONObject json = new JSONObject(response.toString());

            // Extract current weather object
            JSONObject weather = json.getJSONObject("current_weather");

            // Display weather details
            System.out.println("\n----- Current Weather in " + city + " -----");
            System.out.println("Temperature : " + weather.getDouble("temperature") + "°C");
            System.out.println("Windspeed   : " + weather.getDouble("windspeed") + " km/h");
            System.out.println("Time        : " + weather.getString("time"));

        } catch (Exception e) {
            System.out.println("Error fetching weather: " + e.getMessage());
        }
    }
}
