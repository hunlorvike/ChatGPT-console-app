package hung.chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "KEY HERE !!!!!!"; // Thay thế bằng API key của bạn
    private static final String MODEL_NAME = "text-embedding-ada-002"; // Thay thế bằng mô hình của bạn (ví dụ: "gpt-3.5-turbo")

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("You: ");
                String userMessage = reader.readLine();

                if ("exit".equalsIgnoreCase(userMessage)) {
                    System.out.println("Exiting chatbot. Goodbye!");
                    break;
                }

                String chatbotResponse = getChatGPTResponse(userMessage);
                System.out.println("Chatbot: " + chatbotResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getChatGPTResponse(String userMessage) {
        try {
            URL url = new URL(OPENAI_API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);
            con.setDoOutput(true);

            String requestBody = "{\"model\": \"" + MODEL_NAME + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage + "\"}]}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error getting response from ChatGPT.";
        }
    }
}
