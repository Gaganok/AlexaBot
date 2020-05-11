package Database;

import Constants.AlexaConstants;
import Constants.LambdaConstants;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DynamoDBImpl {
    private final AmazonDynamoDB client;
    private final DynamoDB dynamoDB;
    private final Table table;

    public DynamoDBImpl(){
        client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable(LambdaConstants.DATABASE_NAME);
    }

    public Table getDatabaseTable(){
        return table;
    }

    public Item getDatabaseItem(String dataset){
        return table.getItem(LambdaConstants.DATABASE_PRIMARY_KEY, dataset);
    }
    
    public Map<String, Object> getDatabaseData(String dataset) {
    	return (Map<String, Object>) getDatabaseItem(dataset).get(LambdaConstants.DATABASE_DATA);
    }

    public boolean isDatasetValid(String dataset){
        return !(getDatabaseItem(dataset) == null);
    }

    public List<String> getAllDatasets(){
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(LambdaConstants.DATABASE_NAME);

        ScanResult result = client.scan(scanRequest);
        List<String> itemsList = new ArrayList<String>();
        for (Map<String, AttributeValue> item : result.getItems())
            item.keySet().forEach(itemsList::add);

        return itemsList;
    }

    public String getLast5Datasets(){
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(LambdaConstants.DATABASE_NAME)
                .withAttributesToGet(LambdaConstants.DATABASE_PRIMARY_KEY)
                .withLimit(5);

        ScanResult result = client.scan(scanRequest);
        List<String> itemsList = new ArrayList<String>();
        for (Map<String, AttributeValue> item : result.getItems())
            item.forEach((key, value) -> itemsList.add(value.getS()));

        return parseString(itemsList, false);
    }
    
    public String get5Id(String dataset) {
    	Map<String, Object> map = getDatabaseData(dataset);
    	List<String> ids = new ArrayList<String>();
    	Iterator<String> iterator = map.keySet().iterator();
    	for(int i = 0; i < 5; ++i) {
    		if(iterator.hasNext())
    			ids.add(iterator.next());
    		else break;
    	}
    	return parseString(ids, map.keySet().size() > 5);
    }
    
    public String get5Field(String dataset) {
    	Map<String, Object> map = getDatabaseData(dataset);
    	Map<String, String> fieldMap = (Map) map.values().iterator().next();
    	List<String> fields = new ArrayList<String>();
    	Iterator<String> iterator = fieldMap.keySet().iterator();
    	for(int i = 0; i < 5; ++i) {
    		if(iterator.hasNext())
    			fields.add(iterator.next());
    		else break;
    	}
    	return parseString(fields, fieldMap.keySet().size() > 5);
    }
    
    private String parseString(List<String> result, boolean andMore) {
    	
    	String message = "";
    	for(int i = 0; i < result.size(); ++i) {
    		message += result.get(i);
    		if(i < result.size() - 1)
    			message += ", ";
    	}
    	
    	if (andMore)
    		message += AlexaConstants.AND_MORE;
        
        return message;
    }
}
