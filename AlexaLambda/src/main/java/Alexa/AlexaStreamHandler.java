package Alexa;

import Constants.LambdaConstants;
import Handlers.*;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import java.beans.Expression;

public class AlexaStreamHandler extends SkillStreamHandler {
    public AlexaStreamHandler() {
        super(Skills.standard()
                .addRequestHandlers(
                        new LaunchRequestHandler(),
                        new ExitSkillHandler(),
                        new AvailableDatasetRequestHandler(),
                        new AvailableIDRequestHandler(),
                        new AvailableFieldRequestHandler(),
                        new TestRequestHandler(),
                        new ChatRequestHandler(),
                        new DatasetRequestHandler())
                .withSkillId(LambdaConstants.SKILL_ID)
                .build());
    }
}
