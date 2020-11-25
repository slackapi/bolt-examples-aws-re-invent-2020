# Deploying Bolt for Java apps to API Gateway + AWS Lambda

# Create a new Slack app

Refer to [this guide](/README.md).

# How to deploy this app to AWS

## Deploy the app

```bash
# install aws-cli v2 https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-mac.html
aws configure

npm i
export SLACK_BOT_TOKEN=xoxb-
export SLACK_SIGNING_SECRET=
export SERVERLESS_STAGE=dev
export SLS_DEBUG=*
./gradlew clean build &&
  ./node_modules/serverless/bin/serverless.js deploy --stage ${SERVERLESS_STAGE} --verbose &&
  ./node_modules/serverless/bin/serverless.js invoke --stage ${SERVERLESS_STAGE} --function warmup
```

## Configure the *Request URL* for Events API

Refer to [this guide](/README.md). The **Request URL** should be `https://{your domain}.execute-api.{your region}.amazonaws.com/{serverless stage}/slack/events`.

# Local development using Jetty

```bash
export SLACK_BOT_TOKEN=xoxb-
export SLACK_SIGNING_SECRET=
./gradlew run
# run `ngrok http 3000` in another terminal
```