package tv.strohi.stfu.playlistservice.youtube.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class Utils {
    private static final Logger logger = LogManager.getLogger(Utils.class.getCanonicalName());

    public static String readResult(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        logger.debug("read result: {}" + sb.toString());
        return sb.toString();
    }
}
