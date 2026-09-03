/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package promptlab;
import java.net.http.HttpResponse;
/**
 *
 * @author feyza
 */
public class ApiCallResult {

    private HttpResponse<String> response;
    private double latencySeconds;

    public ApiCallResult(HttpResponse<String> response, double latencySeconds) {
        this.response = response;
        this.latencySeconds = latencySeconds;
    }

    public HttpResponse<String> getResponse () {
        return response;
    }

    public double getLatencySeconds() {
        return latencySeconds;
    }
}
