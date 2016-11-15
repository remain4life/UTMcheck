import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ParserUtil {
    private ParserUtil() {
    }

    public static List<URL> parseFile(Path path) throws IOException {


        //our mock
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://127.0.0.1:8080/"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));
        String s;
        while ((s = reader.readLine()) != null) {
            if (s.matches("^http://.*")) {
                urls.add(new URL(s));
            } else if (s.matches("^\\d+.*")) {
                s = "http://" + s;
                urls.add(new URL(s));
            }
        }



        return urls;
    }

    public static String socketToIP(URL socket) {
        return socket.toString().replaceAll("^http://","").replaceAll(":.*$", "");
    }
}
