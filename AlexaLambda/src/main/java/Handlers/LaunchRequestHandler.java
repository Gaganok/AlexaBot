package Handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import Constants.AlexaConstants;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;


public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSpeech(AlexaConstants.LAUNCH)
                .withReprompt(AlexaConstants.REPROMPT)
                .withShouldEndSession(false)
                .build();
    }

}
