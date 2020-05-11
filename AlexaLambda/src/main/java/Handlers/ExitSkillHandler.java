package Handlers;

import Constants.AlexaConstants;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;

import static com.amazon.ask.request.Predicates.intentName;

import com.amazon.ask.model.Response;

import java.util.Optional;

public class ExitSkillHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("AMAZON.StopIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech(AlexaConstants.EXIT)
                .withShouldEndSession(true)
                .build();
    }
}
