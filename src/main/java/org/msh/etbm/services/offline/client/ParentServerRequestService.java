package org.msh.etbm.services.offline.client;

import com.fasterxml.jackson.databind.JavaType;
import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.init.FileDownloadListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

/**
 * Component to request the Parent Server of an off-line mode instance.
 * Created by Mauricio on 21/11/2016.
 */
@Component
public class ParentServerRequestService {

    private static final int BUFFER_SIZE = 65535; //4096

    @Autowired
    SysConfigService configService;

    /**
     * Returns the response of the server service passed on params, converted to the type on type param.
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
        HttpURLConnection conn = getPostConnection(serverUrl, serviceUrl, authToken);
        Object ret = null;

        try {
            // Parse payLoad
            if (payLoad != null) {
                String input = JsonUtils.objectToJSONString(payLoad, false);

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
            }

            checkHttpCode(conn.getResponseCode());

            if (javaType != null) {
                ret = JsonUtils.parse(conn.getInputStream(), javaType);
            } else if (classType != null) {
                ret = JsonUtils.parse(conn.getInputStream(), classType);
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
     * Returns the response of the parent server service converted to the type on type param.
     * @param serviceUrl API service URL stating with /.
     * @param authToken authToken if it is needed to be logged in to have access to API service.
     * @param payLoad object to send inside request as parameter to the service.
     * @param javaType defines the type to be returned in JavaType type. If also inform classType param, javaType will overwrite it.
     * @param classType defines the type to be returned in Class type.  If also inform javaType param, javaType will overwrite it.
     * @param <T>
     * @return
     */
    public <T> T post(String serviceUrl, String authToken, Object payLoad, JavaType javaType, Class<T> classType) {
        String serverUrl = configService.loadConfig().getServerURL();
        return this.post(serverUrl, serviceUrl, authToken, payLoad, javaType, classType);
    }

    /**
     * Asynchronously sends a request to download a file. This file will be stored as a temp.
     * @param serverUrl
     * @param serviceUrl
     * @param authToken
     * @return the file downloaded
     */
    @Async
    public void downloadFile(String serverUrl, String serviceUrl, String authToken, FileDownloadListener listener) {
        URL url = getURL(serverUrl, serviceUrl);
        HttpURLConnection httpConn = null;

        File file = null;

        try {
            httpConn = (HttpURLConnection) url.openConnection();
            if (authToken != null && !authToken.isEmpty()) {
                httpConn.setRequestProperty("X-Auth-Token", authToken);
            }
            checkHttpCode(httpConn.getResponseCode());

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();

            file = File.createTempFile("file", ".etbm");

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(file);

            // read downloaded file
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            throw new SynchronizationException("Error while creating download file from parent server.");
        } finally {
            httpConn.disconnect();
        }

        listener.afterDownload(file);
    }

    /**
     * Creates a POST connection
     * @param serverUrl
     * @param serviceUrl
     * @param authToken
     * @return
     */
    private HttpURLConnection getPostConnection(String serverUrl, String serviceUrl, String authToken) {
        HttpURLConnection conn = null;

        try {
            // Instantiate URL
            URL url = getURL(serverUrl, serviceUrl);

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
     * Instantiate and returns the URL object based on the params
     * @param serverUrl
     * @param serviceUrl
     * @return the URL object based on the params
     */
    private URL getURL(String serverUrl, String serviceUrl) {
        URL url = null;

        try {
            // Instantiate URL
            url = new URL(serverUrl + serviceUrl);
        } catch (MalformedURLException e) {
            throw new SynchronizationException("Error while creating post request to parent server.");
        }

        return url;
    }

    /**
     * Handle HTTP error when HTTP code is different than 200
     * @param responseCode
     */
    private void checkHttpCode(int responseCode) {
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
}
