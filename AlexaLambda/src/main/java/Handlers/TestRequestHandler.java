package Handlers;

import Constants.AlexaConstants;
import Database.DynamoDBImpl;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class TestRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return false;
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        DynamoDBImpl impl = new DynamoDBImpl();
        impl.getAllDatasets();
        impl.getLast5Datasets();
        return input.getResponseBuilder()
                .withSpeech(AlexaConstants.TEST)
                .build();
    }
}
