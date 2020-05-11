package Handlers;

import Constants.AlexaConstants;
import Database.DynamoDBImpl;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class DatasetRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ChatbotDatasetIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        RequestHelper requestHelper = RequestHelper.forHandlerInput(input);
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String,Object> attributes = attributesManager.getSessionAttributes();

        Optional<String> datasetOption = requestHelper.getSlotValue("dataset");

        String responseMessage = AlexaConstants.DATASET_SUCCESS;

        if(datasetOption.isPresent()){
            String datasetName = datasetOption.get();
            DynamoDBImpl dbImpl = new DynamoDBImpl();

            if(dbImpl.isDatasetValid(datasetName))
                attributes.put("dataset", datasetName);
            else
                responseMessage = AlexaConstants.DATASET_FAIL;
        }

        return input.getResponseBuilder()
                .withSpeech(responseMessage)
                .withShouldEndSession(false)
                .build();
    }
}
