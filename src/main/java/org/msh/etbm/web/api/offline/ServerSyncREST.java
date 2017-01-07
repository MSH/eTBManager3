package org.msh.etbm.web.api.offline;

import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by rmemoria on 23/11/16.
 */
@RestController
@RequestMapping(path = "/api/offline/server")
@Authenticated
public class ServerSyncREST {

    @RequestMapping(path = "/sync/startsync", method = RequestMethod.POST)
    public StandardResult handleFileUpload(@RequestParam("file") MultipartFile multiFile) {

        try {
            File file = new File("C:\\Users\\Mauricio\\Documents\\MSH\\AQUI1s23.TXT");
            multiFile.transferTo(file);

            InputStream fileStream = new FileInputStream(file);
            InputStream gfileStream = new GZIPInputStream(fileStream);

            String result = getStringFromInputStream(gfileStream);

            System.out.println(result);

            System.out.println("File saved");

            fileStream.close();
            gfileStream.close();

            file.delete();

            System.out.println("File deleted");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return StandardResult.createSuccessResult();
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }


}
