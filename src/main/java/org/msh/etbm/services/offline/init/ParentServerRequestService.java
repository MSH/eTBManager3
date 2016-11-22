package org.msh.etbm.services.offline.init;

import com.fasterxml.jackson.databind.JavaType;
import org.msh.etbm.commons.JsonParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Component to request the Parent Server of an off-line mode instance.
 * Created by Mauricio on 21/11/2016.
 */
@Component
public class ParentServerRequestService {

    /**
     * Returns the response of the parent server service converted to the type on type param.
     * @param serverUrl
     * @param serviceUrl
     * @param authToken
     * @param payLoad
     * @param type
     * @param <T>
     * @return
     */
    public <T> T post(String serverUrl, String serviceUrl, String authToken, Object payLoad, JavaType type) {
        HttpURLConnection conn = getPostConnection(serverUrl, serviceUrl, authToken);
        Object ret = null;

        try {
            // Parse payLoad
            String input = JsonParser.objectToJSONString(payLoad, false);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            checkHttpCode(conn.getResponseCode());

            ret = JsonParser.parse(conn.getInputStream(), type);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return (T) ret;
    }

    /**
     * Returns the response of the parent server service converted to the type on type param.
     * @param serverUrl
     * @param serviceUrl
     * @param authToken
     * @param payLoad
     * @param type
     * @param <T>
     * @return
     */
    public <T> T post(String serverUrl, String serviceUrl, String authToken, Object payLoad, Class<T> type) {
        HttpURLConnection conn = getPostConnection(serverUrl, serviceUrl, authToken);
        Object ret = null;

        try {
            // Parse payLoad
            String input = JsonParser.objectToJSONString(payLoad, false);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            checkHttpCode(conn.getResponseCode());

            ret = JsonParser.parse(conn.getInputStream(), type);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return (T) ret;
    }

    /**
     * Creates a POST connection
     * @param serverUrl
     * @param serviceUrl
     * @param authToken
     * @return
     */
    private HttpURLConnection getPostConnection(String serverUrl, String serviceUrl, String authToken){
        HttpURLConnection conn = null;

        try {
            // Instantiate URL
            URL url = new URL(serverUrl + serviceUrl);

            // Configure Request
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            if (authToken != null && !authToken.isEmpty()) {
                conn.setRequestProperty("X-Auth-Token", authToken); // TODOMS: nao testado
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
        }

        return conn;
    }

    /**
     * Handle HTTP error when HTTP code is different than 200
     * @param responseCode
     */
    private void checkHttpCode(int responseCode) {
        if (responseCode != 200) {
            // TODOMS: handle the HTTP errors
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }
}
