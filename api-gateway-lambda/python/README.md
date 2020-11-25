# Deploying Bolt for Python apps to API Gateway + AWS Lambda

# Create a new Slack app

Refer to [this guide](/README.md).

## Grab the credentials

* SLACK_SIGNING_SECRET: *Settings* > *Basic Information* > *App Credentials* > *Signing Secret*
* SLACK_BOT_TOKEN: *Settings* > *Install App* > *OAuth Tokens for Your Team* > *Bot User OAuth Access Token*

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
python3 -m venv .venv
source .venv/bin/activate
pip install -U pip
pip install -U -r requirements.txt -t lib
./node_modules/serverless/bin/serverless.js deploy --stage ${SERVERLESS_STAGE} --verbose
```

## Configure the *Request URL* for Events API

Refer to [this guide](/README.md). The **Request URL** should be `https://{your domain}.execute-api.{your region}.amazonaws.com/{serverless stage}/slack/events`.

# Local development using Jetty

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -U pip boto3
pip install -r requirements.txt
export SLACK_SIGNING_SECRET=***
export SLACK_BOT_TOKEN=xoxb-***
python handler.py
# run `ngrok http 3000` in another terminal
```