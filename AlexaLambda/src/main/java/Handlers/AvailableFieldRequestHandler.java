package Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;

import Constants.AlexaConstants;
import Database.DynamoDBImpl;

public class AvailableFieldRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ChatbotAvailableFieldIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
    	RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
    	AttributesManager attributesManager = input.getAttributesManager();
        Map<String,Object> sessionAttributes = attributesManager.getSessionAttributes();
        String dataset = (String) sessionAttributes.get("dataset");
        
        String resultMessage = AlexaConstants.DATASET_NOT_SELECTED;
        if(dataset != null) {
        	DynamoDBImpl dbImpl = new DynamoDBImpl();
            String result = dbImpl.get5Field(dataset);
            result = result.isEmpty() ? "None" : result;
            resultMessage = AlexaConstants.FIELD_AVAILABLE + result;
        }
        
        return input.getResponseBuilder()
                .withSpeech(resultMessage)
                .withShouldEndSession(false)
                .build();
    }


}
