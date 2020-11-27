package hello;

import com.slack.api.bolt.aws_lambda.SlackApiLambdaHandler;
import com.slack.api.bolt.aws_lambda.request.ApiGatewayRequest;

public class SlackEventHandler extends SlackApiLambdaHandler {

    public SlackEventHandler() {
        super(SlackApp.get());
    }

    @Override
    protected boolean isWarmupRequest(ApiGatewayRequest awsReq) {
        return awsReq.getBody() != null && awsReq.getBody().equals("warmup=true");
    }
}