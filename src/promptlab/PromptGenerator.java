/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package promptlab;

/**
 *
 * @author feyza
 */
public class PromptGenerator {
    
    public static String createRoleContextGoalPrompt(String originaPrompt) {
        return """
                Role:
                You are an experienced and patient instructor.

                Context:
                The user is a software engineering student who is learning the topic.

                Goal:
                Explain the requested topic clearly and at a beginner-friendly level.

                User Task:
                %s
                """.formatted(originaPrompt);
    }
    
    
    
    
}
