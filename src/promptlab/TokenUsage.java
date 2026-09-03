/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package promptlab;

/**
 *
 * @author feyza
 */
public class TokenUsage {
    private int promptTokens;
    private int completionTokens;
            private int totalTokens;
            
            
    public TokenUsage(int promptTokens, int completionTokens, int totalTokens) {
        this.promptTokens=promptTokens;
        this.completionTokens=completionTokens;
        this.totalTokens=totalTokens;
    }   
    
    public int getPromptTokens() {
        return promptTokens;
    }
    
    public int getCompletionTokens() {
        return completionTokens;
    }
    
    public int getTotalTokens() {
        return totalTokens;
    }
}
