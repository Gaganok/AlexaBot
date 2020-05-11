package helloworld;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

import Parser.Parser;
import Parser.JsonParser;
import Parser.CSVParser;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<S3Event, String> {

    public String handleRequest(S3Event s, final Context context) {
        for(S3EventNotification.S3EventNotificationRecord record : s.getRecords()) {
            String src = record.getS3().getObject().getUrlDecodedKey();
            int extensionIndex = src.lastIndexOf('.');
            String extension = src.substring(extensionIndex + 1);
            String fileName = src.substring(0, extensionIndex);

            Parser parser = null;
            if (extension.equalsIgnoreCase("json")) parser = new JsonParser();
            else if (extension.equalsIgnoreCase("csv")) parser = new CSVParser();
            else return "Invalid FIle Format";

            String srcBucker = record.getS3().getBucket().getName();
            AmazonS3 client = AmazonS3ClientBuilder.defaultClient();
            S3Object s3Object = client.getObject(new GetObjectRequest(srcBucker, src));
            InputStream inputStream = s3Object.getObjectContent();

            DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
            Table table = dynamoDB.getTable(System.getenv("database_name"));

            try {
                Item item = parser.parser(inputStream);
                item.withPrimaryKey(System.getenv("database_primary_key"), fileName);
                table.putItem(item);
            }catch (Exception e) {return  e.getMessage();}
        }
        return "Ok";
    }


}
