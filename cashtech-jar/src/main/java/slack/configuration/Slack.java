package slack.configuration;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Slack {
    private static HttpClient client = HttpClient.newHttpClient();
    private static String url = "";

    public static void sendMessage(JSONObject content) throws Exception, IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(content.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Resposta: " + response.statusCode());
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Slack.url = url;
    }
}
