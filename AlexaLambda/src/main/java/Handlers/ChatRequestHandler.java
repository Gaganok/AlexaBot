package Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import com.amazonaws.services.dynamodbv2.document.Item;

import Constants.AlexaConstants;
import Database.DynamoDBImpl;

public class ChatRequestHandler implements IntentRequestHandler  {

	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intent) {
		return input.matches(intentName("ChatbotRequestIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intent) {
		RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
        AttributesManager attributesManager = input.getAttributesManager();
        
        Map<String,Object> sessionAttributes = attributesManager.getSessionAttributes();

        String dataset = (String) sessionAttributes.get("dataset");
        String resultMessage = AlexaConstants.DATASET_NOT_SELECTED;
        
        if(dataset != null) {
        	Optional<String> idOption = requestHelper.getSlotValue("id");
        	Optional<String> fieldOption = requestHelper.getSlotValue("field");
        	
        	if(idOption.isPresent() && fieldOption.isPresent()) {	
        		String id = idOption.get().toLowerCase();
        		String field = fieldOption.get().toLowerCase();
        		
        		DynamoDBImpl impl = new DynamoDBImpl();
        		Map<String, Object> item = impl.getDatabaseData(dataset);
        		Map<String, String> itemMap = (Map) item.get(id);
        		
        		if(itemMap == null) {
        			resultMessage = idOption.get() + AlexaConstants.INVALID_ID;
        		} else {
        			String result = itemMap.get(field);
        			if(result == null) {
        				resultMessage = fieldOption.get() + AlexaConstants.INVALID_FIELD;
        			} else {
        				resultMessage = AlexaConstants.VALID_OTPUT + id + " " + field + " " + result;
        			}
        		}
        	} else {
        		return input.getResponseBuilder()
        				.addDelegateDirective(intent.getIntent())
                        .withShouldEndSession(false)
                        .build();
        	}
        } 
        
        return input.getResponseBuilder()
                .withSpeech(resultMessage)
                .withReprompt(AlexaConstants.REPROMPT)
                .withShouldEndSession(false)
                .build();
	}
}
