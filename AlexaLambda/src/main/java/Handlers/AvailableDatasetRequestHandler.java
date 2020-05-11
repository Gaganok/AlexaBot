package Handlers;

import Constants.AlexaConstants;
import Database.DynamoDBImpl;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class AvailableDatasetRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ChatbotAvailableDatasetIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        DynamoDBImpl dbImpl = new DynamoDBImpl();
        String result = dbImpl.getLast5Datasets(); 
        String resultMessage = result.isEmpty() ? AlexaConstants.DATASET_NOT_AVAILABLE :
        	AlexaConstants.DATASET_AVAILABLE + result;
          
        return input.getResponseBuilder()
                .withSpeech(resultMessage)
                .withShouldEndSession(false)
                .build();
    }
}
