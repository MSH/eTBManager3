package org.msh.etbm.services.offline.client;

import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.offline.SynchronizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 05/01/2017.
 */
@Service
public class ParentServerFileSender {

    private static final String LINE_FEED = "\r\n";

    @Autowired
    SysConfigService configService;

    public <T> T sendFile(String serviceUrl, String authToken, File file, Class<T> classType) {
        // define unique boundary to be used on the whole process
        String boundary = "===" + System.currentTimeMillis() + "===";

        String serverUrl = getServerURL();
        // create post connection
        HttpURLConnection httpConn = getPostConnection(serverUrl, serviceUrl, authToken, boundary);

        Object ret = null;

        try {
            OutputStream outputStream = null;

            try {
                outputStream = httpConn.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream), true);

                addFileOnRequest(file, boundary, writer, outputStream);

                writer.append(LINE_FEED).flush();
                writer.append("--" + boundary + "--").append(LINE_FEED);
                writer.close();

                // checks server's status code first
                checkHttpCode(httpConn.getResponseCode());

                ret = JsonUtils.parse(httpConn.getInputStream(), classType);
            } finally {
                outputStream.close();
            }

        } catch (IOException e) {

        } finally {
            httpConn.disconnect();
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
    private HttpURLConnection getPostConnection(String serverUrl, String serviceUrl, String authToken, String boundary) {
        try {
            URL url = getURL(serverUrl, serviceUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            if (authToken != null || !authToken.isEmpty()) {
                httpConn.setRequestProperty("X-Auth-Token", authToken);
            }

            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            return httpConn;
        } catch (Exception e) {
            throw new SynchronizationException("Error while creating post request to parent server.");
        }
    }

    /**
     * Instantiate and returns the URL object based on the params
     * @param serverUrl
     * @param serviceUrl
     * @return the URL object based on the params
     */
    private URL getURL(String serverUrl, String serviceUrl) {
        URL url;

        try {
            // Instantiate URL
            url = new URL(serverUrl + serviceUrl);
        } catch (MalformedURLException e) {
            throw new SynchronizationException("Error while creating post request to parent server.");
        }

        return url;
    }

    /**
     * Adds a upload file section to the request
     * @param file a File to be uploaded
     * @throws IOException
     */
    public void addFileOnRequest(File file, String boundary, PrintWriter writer, OutputStream outputStream) throws IOException {
        String fieldName = "file";
        FileInputStream inputStream = new FileInputStream(file);

        try {
            String fileName = file.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {

        } finally {
            inputStream.close();
        }
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

    private String getServerURL() {
        return configService.loadConfig().getServerURL();
    }

}
