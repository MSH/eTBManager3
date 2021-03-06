package org.msh.etbm.services.offline.client;

import com.fasterxml.jackson.databind.JavaType;
import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.commons.ValidationException;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.listeners.FileDownloadListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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
    public <T> T post(String serverUrl, String serviceUrl, String authToken, Object payLoad, JavaType javaType, Class<T> classType)
        throws IOException {

        HttpURLConnection conn = getConnection("POST", serverUrl, serviceUrl, authToken);
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
    public <T> T post(String serviceUrl, String authToken, Object payLoad, JavaType javaType, Class<T> classType) throws IOException {
        return this.post(getServerURL(), serviceUrl, authToken, payLoad, javaType, classType);
    }

    public <T> T get(String serviceUrl, String authToken, JavaType javaType, Class<T> classType) throws IOException {
        String serverUrl = getServerURL();

        HttpURLConnection conn = getConnection("GET", serverUrl, serviceUrl, authToken);
        Object ret = null;

        try {
            checkHttpCode(conn.getResponseCode());

            if (javaType != null) {
                ret = JsonUtils.parse(conn.getInputStream(), javaType);
            } else if (classType != null) {
                ret = JsonUtils.parse(conn.getInputStream(), classType);
            } else {
                throw new SynchronizationException("Must inform javaType or classType param");
            }

        } finally {
            conn.disconnect();
        }

        return (T) ret;
    }

    /**
     * Asynchronously sends a request to download a file. This file will be stored as a temp.
     * @param serverUrl
     * @param serviceUrl
     * @param authToken
     * @return the file downloaded
     */
    @Async
    public void downloadFile(String serverUrl, String serviceUrl, String authToken, FileDownloadListener listener) throws IOException {
        boolean success = true;

        URL url = new URL(serverUrl + serviceUrl);
        HttpURLConnection httpConn = null;

        File file = null;

        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            httpConn = (HttpURLConnection) url.openConnection();

            if (authToken != null && !authToken.isEmpty()) {
                httpConn.setRequestProperty("X-Auth-Token", authToken);
            }
            checkHttpCode(httpConn.getResponseCode());

            // opens input stream from the HTTP connection
            inputStream = httpConn.getInputStream();

            file = File.createTempFile("file", ".etbm");

            // opens an output stream to save into file
            outputStream = new FileOutputStream(file);

            // read downloaded file
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch (Exception e) {
            success = false;
            throw new SynchronizationException(e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

            httpConn.disconnect();
        }

        listener.afterDownload(file, success);
    }

    @Async
    public void downloadFile(String serviceUrl, String authToken, FileDownloadListener listener) throws IOException {
        this.downloadFile(getServerURL(), serviceUrl, authToken, listener);
    }

    /**
     * Creates a HttpURLConnection connection
     * @param requestMethod
     * @param serverUrl
     * @param serviceUrl
     * @param authToken
     * @return
     */
    private HttpURLConnection getConnection(String requestMethod, String serverUrl, String serviceUrl, String authToken) throws IOException {
        // Instantiate URL
        URL url = new URL(serverUrl + serviceUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {

            // Configure Request
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Content-Type", "application/json");
            if (authToken != null && !authToken.isEmpty()) {
                conn.setRequestProperty("X-Auth-Token", authToken);
            }
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
        if (responseCode != 200) {
            switch (responseCode) {
                case 404:
                    throw new ValidationException(null, "init.offinit.error1");
                case 403:
                case 401:
                    throw new ValidationException(null, "login.invaliduserpwd");
                default:
                    throw new SynchronizationException("Failed to request parent server: HTTP error code " + responseCode);
            }
        }
    }

    private String getServerURL() {
        return configService.loadConfig().getServerURL();
    }
}
