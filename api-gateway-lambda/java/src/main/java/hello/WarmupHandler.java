package hello;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.slack.api.bolt.aws_lambda.request.ApiGatewayRequest;
import com.slack.api.bolt.aws_lambda.response.ApiGatewayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class WarmupHandler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarmupHandler.class);

    // refer to serverless.yml
    private static final String SERVERLESS_SERVICE = "my-bolt-app";
    private static final String SERVERLESS_STAGE = System.getenv("SERVERLESS_STAGE");

    private List<String> functionNames = Arrays.asList(SERVERLESS_SERVICE + "-" + SERVERLESS_STAGE + "-api");

    @Override
    public ApiGatewayResponse handleRequest(ApiGatewayRequest req, Context context) {
        LOGGER.info("received: {}", req);
        var lambda = AWSLambdaClient.builder().withRegion(Regions.US_EAST_1.getName()).build();
        for (String functionName : functionNames) {
            var invokeReq = new InvokeRequest().withFunctionName(functionName).withPayload("{\"body\":\"warmup=true\"}");
            var result = lambda.invoke(invokeReq);
            LOGGER.info("Warmup the function: {}, result: {}", functionName, result);
            if (result.getStatusCode() != 200 || result.getFunctionError() != null) {
                throw new RuntimeException("status: " + result.getStatusCode() + " function error: " + result.getFunctionError());
            }
        }
        return ApiGatewayResponse.builder().statusCode(200).rawBody("done").build();
    }
}