/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package promptlab;

import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class PromptLabApp {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        sayWelcomeMessage();
        showMenu();

        System.out.println("Enter your choice: ");
        int choice = input.nextInt();

        input.nextLine();

        handleChoice(choice, input);
        input.close();
    }

    public static void sayWelcomeMessage() {
        System.out.println("===PROMPT LAB===");
        System.out.println("Prompt engineering Playground");
        System.out.println();

    }

    public static void showMenu() {
        System.out.println();
        System.out.println("Select a prompt technique:");
        System.out.println("1 - Role / Context / Goal");
        System.out.println("2 - Constraints");
        System.out.println("3 - Output Format");
    }

    public static void handleChoice(int choice, Scanner input) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        System.out.println();
        switch (choice) {
            case 1: {
                System.out.println("RCG selected.");

                System.out.println("Enter your prompt: ");
                String originalPrompt = input.nextLine();
                System.out.println();

                String engineeredPrompt
                        = PromptGenerator.createRoleContextGoalPrompt(originalPrompt);

// for original
long originalStartTime = System.nanoTime();
                HttpResponse<String> originalResponse = sendPrompt(originalPrompt, client);
long originalEndTime = System.nanoTime();
double originalLatencySeconds = (originalEndTime - originalStartTime) / 1_000_000_000.0;
                printPromptResult("Original", originalPrompt, originalResponse, originalLatencySeconds);

//for engineered
long engineeredStartTime=System.nanoTime();
                HttpResponse<String> engineeredResponse = sendPrompt(engineeredPrompt, client);
long engineeredEndTime=System.nanoTime();
double engineeredLatencySeconds=(engineeredEndTime-engineeredStartTime) / 1_000_000_000.0;
                printPromptResult("Engineered", engineeredPrompt, engineeredResponse, engineeredLatencySeconds);

                break;
            }
            case 2: {
                System.out.println("Constraints selected.");

                System.out.println("Enter your prompt: ");
                String originalPrompt = input.nextLine();
                System.out.println();

                String engineeredPrompt = PromptGenerator.createConstraintsPrompt(originalPrompt);

// for original
long originalStartTime = System.nanoTime();
                HttpResponse<String> originalResponse = sendPrompt(originalPrompt, client);
long originalEndTime = System.nanoTime();
double originalLatencySeconds = (originalEndTime - originalStartTime) / 1_000_000_000.0;
                printPromptResult("Original", originalPrompt, originalResponse, originalLatencySeconds);

//for engineered
long engineeredStartTime=System.nanoTime();
                HttpResponse<String> engineeredResponse = sendPrompt(engineeredPrompt, client);
long engineeredEndTime=System.nanoTime();
double engineeredLatencySeconds=(engineeredEndTime-engineeredStartTime) / 1_000_000_000.0;

    String engineeredText=parseResponse(engineeredResponse.body());
    int wordCount=countWords(engineeredText);
                printPromptResult("Engineered", engineeredPrompt, engineeredResponse, engineeredLatencySeconds);

            System.out.println("Maximum Words: 300");
            System.out.println("Generated Words: "+wordCount);
                if(wordCount<=300) {
                    System.out.println("Word Limit: PASSED");
                }
                else {
                    System.out.println("Word Limit: VIOLATED");
                }
                break;
            }
            
            case 3: {
                System.out.println("Output format selected.");

                System.out.println("Enter your prompt: ");
                String originalPrompt = input.nextLine();
                System.out.println();

                String engineeredPrompt = PromptGenerator.createOutputFormatPrompt(originalPrompt);

// for original
long originalStartTime = System.nanoTime();
                HttpResponse<String> originalResponse = sendPrompt(originalPrompt, client);
long originalEndTime = System.nanoTime();
double originalLatencySeconds = (originalEndTime - originalStartTime) / 1_000_000_000.0;
                printPromptResult("Original", originalPrompt, originalResponse, originalLatencySeconds);

//for engineered
long engineeredStartTime=System.nanoTime();
                HttpResponse<String> engineeredResponse = sendPrompt(engineeredPrompt, client);
long engineeredEndTime=System.nanoTime();
double engineeredLatencySeconds=(engineeredEndTime-engineeredStartTime) / 1_000_000_000.0;

                printPromptResult("Engineered", engineeredPrompt, engineeredResponse, engineeredLatencySeconds);
                
                String engineeredText=parseResponse(engineeredResponse.body());
            boolean hasShortAnswer=engineeredText.contains("Short Answer:");
            boolean hasKeyPoints=engineeredText.contains("Key Points:");
            boolean hasExample=engineeredText.contains("Example:");
            boolean hasFinalSummary=engineeredText.contains("Final Summary:");
            
                System.out.println("---Output Format Validation---");
                if(hasShortAnswer && hasKeyPoints && hasExample && hasFinalSummary) {
                    System.out.println("Output Format: PASSED");
                }
                else {
                    System.out.println("Output FOrmat: VIOLATED");
                }
                
                break;
            }

            
            default:
                System.out.println("Invalid choice.");

        }
    }

    public static String createRequestBody(String prompt) {
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        JSONArray messages = new JSONArray();
        messages.put(message);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "qwen/qwen3.6-27b");
        requestBody.put("messages", messages);
        requestBody.put("reasoning_effort", "none");

        return requestBody.toString();
    }

    public static HttpRequest createHttpRequest(String jsonBody) {
        String apiKey = System.getenv("GROQ_API_KEY");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return request;

    }

    public static HttpResponse<String> sendPrompt(String prompt, HttpClient client) throws Exception {

        String jsonBody = createRequestBody(prompt);

        HttpRequest request
                = createHttpRequest(jsonBody);

       
        HttpResponse<String> response
                = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );
       
        return response;
    }
    
    public static String parseResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);

        JSONArray choices = jsonResponse.getJSONArray("choices");

        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        String content = message.getString("content");
        return content;

    }
    
    public static TokenUsage parseTokenUsage(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONObject usage = jsonResponse.getJSONObject("usage");
        int promptTokens=usage.getInt("prompt_tokens");
        int completionTokens=usage.getInt("completion_tokens");
        int totalTokens=usage.getInt("total_tokens");
                
        TokenUsage tokenUsage=new TokenUsage(promptTokens,completionTokens, totalTokens);
        return tokenUsage;
    }
    
    public static int countWords(String text) {
        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    public static void printPromptResult(String label, String prompt, HttpResponse<String> response,
            double latencySeconds) {

        String llmResponse = parseResponse(response.body());
        
        TokenUsage parsedUsage=parseTokenUsage(response.body());
        
        System.out.println("---" + label + " Token Usage---");
        System.out.println("Prompt Tokens: " + parsedUsage.getPromptTokens());
        System.out.println("Completion Tokens: " + parsedUsage.getCompletionTokens());
        System.out.println("Total Tokens: " + parsedUsage.getTotalTokens());
        System.out.println();
        
        System.out.println("---" + label + " Latency---");
        System.out.println(latencySeconds +" seconds");
        System.out.println();

        System.out.println("---" + label + " Prompt---");
        System.out.println(prompt);
        System.out.println();

        System.out.println("---" + label + " Status Code---");
        System.out.println(response.statusCode());
        System.out.println();

        System.out.println("---" + label + " Raw Response Body---");
        System.out.println(response.body());
        System.out.println();

        System.out.println("---" + label + " LLM Response---");
        System.out.println(llmResponse);
        System.out.println();
        System.out.println();
    }

}
