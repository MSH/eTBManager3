package org.msh.etbm.services.offline;

import com.fasterxml.jackson.databind.JavaType;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.sync.SynchronizationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

/**
 * Component to request the Parent Server of an off-line mode instance.
 * Created by Mauricio on 21/11/2016.
 */
@Component
public class ParentServerRequestService {

    /**
     * Returns the response of the parent server service converted to the type on type param.
     * @param serverUrl etbm3 URL.
     * @param serviceUrl API service URL stating with /.
     * @param authToken authToken if it is needed to be logged in to have access to API service.
     * @param payLoad object to send inside request as parameter to the service.
     * @param javaType defines the type to be returned in JavaType type. If also inform classType param, javaType will overwrite it.
     * @param classType defines the type to be returned in Class type.  If also inform javaType param, javaType will overwrite it.
     * @param <T>
     * @return
     */
    public <T> T post(String serverUrl, String serviceUrl, String authToken, Object payLoad, JavaType javaType, Class<T> classType) {
        String a = checkServerAddress(serverUrl);
        HttpURLConnection conn = getPostConnection(a, serviceUrl, authToken);
        Object ret = null;

        try {
            // Parse payLoad
            String input = JsonParser.objectToJSONString(payLoad, false);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            checkHttpCode(conn.getResponseCode());

            if (javaType != null) {
                ret = JsonParser.parse(conn.getInputStream(), javaType);
            } else if (classType != null) {
                ret = JsonParser.parse(conn.getInputStream(), classType);
            } else {
                throw new SynchronizationException("Must inform javaType or classType param");
            }

        } catch (UnknownHostException e) {
            throw new EntityValidationException(new Object(), null, null, "init.offinit.error1");
        } catch (IOException e) {
            throw new SynchronizationException("Error while requesting parent server");
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
                conn.setRequestProperty("X-Auth-Token", authToken);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Error while creating post request to parent server.");
        } finally {
            conn.disconnect();
        }

        return conn;
    }

    /**
     * Handle HTTP error when HTTP code is different than 200
     * @param responseCode
     */
    private void checkHttpCode(int responseCode) {
        // TODOMS: handle http errors
        if (responseCode != 200) {
            switch (responseCode) {
                case 404:
                    throw new EntityValidationException(new Object(), null, null, "init.offinit.error1");
                case 403:
                case 401:
                    throw new EntityValidationException(new Object(), null, null, "login.invaliduserpwd");
                default:
                    throw new SynchronizationException("Failed to request parent server: HTTP error code " + responseCode);
            }
        }
    }

    /**
     * Check if the server address is incomplete. Append the name 'etbm3'
     * at the end of address and the protocol 'http://', if missing
     *
     * @param url is the address of eTB Manager web version
     * @return the address with complements
     */
    private String checkServerAddress(String url) {
        String server = url;
        // try to fill gaps in the composition of the server address
        if (!server.startsWith("http")) {
            server = "http://" + server;
        }

        if (!server.endsWith("etbm3") && !server.endsWith("etbm3/")) {
            if ((!server.endsWith("/")) || (!server.endsWith("\\"))) {
                server += "/";
            }
            server += "etbm3";
        }
        return server;
    }
}
