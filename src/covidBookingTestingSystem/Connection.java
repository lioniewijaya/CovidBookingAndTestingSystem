package CovidBookingTestingSystem;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * CovidBookingTestingSystem.Connection class to connect and make request provided by web service.
 */
public class Connection {
    private String rootUrl = System.getenv("ROOT_URL");   // Web service's root URL
    private String apiKey = System.getenv("API_KEY");     // API key
    private HttpClient client = HttpClient.newHttpClient();     // Client
    private HttpRequest request;                                // Request
    private HttpResponse response;                              // Response

    /**
     * Make a post request to web service.
     * @param url additional url to append to root URL
     * @param jsonString json string provided to web service
     * @return response from web service
     */
    public HttpResponse postRequest(String url, String jsonString) throws IOException, InterruptedException {
        request = HttpRequest.newBuilder(URI.create(rootUrl+url))
                .setHeader("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    /**
     * Make a get request to web service.
     * @param url additional url to append to root URL
     * @return response from web service
     */
    public HttpResponse getRequest(String url) throws IOException, InterruptedException {
        request = HttpRequest.newBuilder(URI.create(rootUrl+url))
                .setHeader("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    /**
     * Make a patch request to web service.
     * @param url additional url to append to root URL
     * @param jsonString json string provided to web service
     * @return response from web service
     */
    public HttpResponse patchRequest(String url, String jsonString) throws IOException, InterruptedException {
        request = HttpRequest.newBuilder(URI.create(rootUrl+url))
                .setHeader("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    /**
     * Make a delete request to web service.
     * @param url additional url to append to root URL
     * @return response from web service
     */
    public HttpResponse deleteRequest(String url) throws IOException, InterruptedException {
        request = HttpRequest.newBuilder(URI.create(rootUrl+url))
                .setHeader("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    /**
     * Make a put request to web service.
     * @param url additional url to append to root URL
     * @param jsonString json string provided to web service
     * @return response from web service
     */
    public HttpResponse putRequest(String url, String jsonString) throws IOException, InterruptedException {
        request = HttpRequest.newBuilder(URI.create(rootUrl+url))
                .setHeader("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
