package util.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class FastJsonUtil {

    public static String readJSONString(HttpServletRequest request){

        // 因为前台向后端传json时，用的是流的方式
        // 所以将流写入一个String中，即json

        StringBuilder json = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return json.toString();
    }
}
