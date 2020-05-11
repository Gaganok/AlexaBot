package Parser;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class JsonParser implements Parser {
    @Override
    public Item parser(InputStream inputStream) throws Exception {
        String jsonString = "";
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine())
                jsonString += scanner.nextLine();
            
            //Work around chatbot lower case standards 
            char[] chArray = jsonString.toCharArray();
            for(int i=0; i < chArray.length;++i) {
            	char ch = chArray[i];
            	if(Character.isAlphabetic(ch))
            		chArray[i] = Character.toLowerCase(ch);
            }
            
            jsonString = new String(chArray);
        }catch (Exception e){throw new Exception("Parser Error");};
        //Map jsonMap = new ObjectMapper().readValue(jsonString, Map.class);
        //return new Item().withMap(itemId, jsonMap);
        return new Item().withJSON(itemId, jsonString);
    }

}
