package Handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class AlexaRequestHandler extends LaunchRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("DatasetIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {

        return handlerInput.getResponseBuilder()
                .withSpeech("Welcome")
                .build();
    }
}