package utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestBodyParser {

    public static String parse(HttpServletRequest req) throws IOException {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();

        InputStream inputStream = req.getInputStream();
        if (inputStream == null) return null;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}
