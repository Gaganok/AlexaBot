package Parser;

import com.amazonaws.services.dynamodbv2.document.Item;

import java.io.InputStream;
import java.util.Map;

public interface Parser {
    final String itemId = System.getenv("database_file_id");
    public Item parser(InputStream inputStream) throws Exception;
}
