package Parser;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CSVParser implements Parser {

    @Override
    public Item parser(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String[] header = bufferedReader.readLine().split(",");
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        bufferedReader.lines().forEach(line -> {
            String[] params = line.split(",");
            Map<String, String> map = new HashMap<String, String>();
            for(int i = 1; i < params.length; i++)
                map.put(header[i].toLowerCase(), params[i].toLowerCase());
            paramMap.put(params[0].toLowerCase(), map);
        });

        return new Item().withMap(itemId, paramMap);
    }
}
