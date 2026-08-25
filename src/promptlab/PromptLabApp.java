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
/**
 *
 * @author feyza
 */
public class PromptLabApp {

    public static void main(String[] args) throws Exception {
       Scanner input=new Scanner(System.in);
       
       sayWelcomeMessage();
       showMenu();
       
       System.out.println("Enter your choice: ");
       int choice=input.nextInt();
       
       input.nextLine();
       
       handleChoice(choice,input);
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
        System.out.println("2 - Constraints / Examples");
        System.out.println("3 - Output Format");
    }
    
    public static void handleChoice(int choice, Scanner input) throws Exception {
        System.out.println();
        switch (choice) {
            case 1:
                System.out.println("RCG selected.");

                System.out.println("Enter your prompt: ");
                String originalPrompt = input.nextLine();
                System.out.println();

                String engineeredPrompt
                        = PromptGenerator.createRoleContextGoalPrompt(originalPrompt);
                
                HttpClient client = HttpClient.newHttpClient();

                String jsonBody=createRequestBody(engineeredPrompt);
                
                HttpRequest request = createHttpRequest(jsonBody);
                
                  HttpResponse<String> response =
            client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );


                System.out.println("---Original Prompt---");
                System.out.println(originalPrompt);
                System.out.println();

                System.out.println("---Engineered Prompt---");
                System.out.println(engineeredPrompt);
                
                System.out.println("---Status Code---");
                System.out.println(response.statusCode());
                
                System.out.println("---Raw Response Body---");
                System.out.println(response.body());

                break;

            case 2:
                System.out.println("Constraints / Examples selected.");
                break;

            case 3:
                System.out.println("Output format selected.");
                break;

            default:
                System.out.println("Invalid choice.");

        }
    }

    public static String createRequestBody(String prompt) {
        JSONObject message=new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        
        JSONArray messages=new JSONArray();
        messages.put(message);
        
        
        JSONObject requestBody=new JSONObject();
        requestBody.put("model", "qwen/qwen3.6-27b");
    requestBody.put("messages", messages);

    return requestBody.toString();
    }
    
   public static HttpRequest createHttpRequest(String jsonBody) { String apiKey = System.getenv("GROQ_API_KEY");

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

    return request;
      
   } 
}
