# Deploying Bolt for Python apps to API Gateway + AWS Lambda

# Create a new Slack app

## Create and enable a Slack app

* Go to https://api.slack.com/apps
* Create a new Slack app
* Go to *Features* > *OAuth & Permissions*
* Add `app_mentions:read` to *Scopes* > *Bot Token Scopes*
* Add `chat:write` to *Scopes* > *Bot Token Scopes*
* Go to *Settings* > *Install App*
* Click *Install to Workspace*
* Complete the installation process

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

aws lightsail create-container-service --service-name my-bolt-app --power nano --scale 1
docker build . -t my-bolt-app
aws lightsail push-container-image --service-name my-bolt-app --image my-bolt-app:latest --label this-is-it

aws lightsail get-container-services
aws lightsail get-container-images --service-name my-bolt-app

echo '{
  "serviceName": "my-bolt-app",
  "containers": {
     "my-bolt-app": {
        "image": ":my-bolt-app.this-is-it",
        "ports": {
           "80": "HTTP"
        },
          "environment": {
            "SLACK_BOT_TOKEN": "__SLACK_BOT_TOKEN__",
            "SLACK_SIGNING_SECRET": "__SLACK_SIGNING_SECRET__"
        }
     }
  },
  "publicEndpoint": {
     "containerName": "my-bolt-app",
     "containerPort": 80
  }
}' | sed -e "s/__SLACK_BOT_TOKEN__/$SLACK_BOT_TOKEN/" | sed -e "s/__SLACK_SIGNING_SECRET__/${SLACK_SIGNING_SECRET}/" > lc.json
aws lightsail create-container-service-deployment --cli-input-json file://lc.json

aws lightsail get-container-service-deployments --service-name my-bolt-app
aws lightsail get-container-log --service-name my-bolt-app --container-name my-bolt-app
```

## Configure the *Request URL* for Events API

* Go to https://api.slack.com/apps and choose your app
* Go to *Features* > *Event Subscriptions*
* Turn the feature on
* Set *Request URL* as `https://{your domain}.execute-api.us-east-1.amazonaws.com/dev/slack/events`
* Click *Subscribe to bot events*
* Add `app_mention` event
* Click *Save Changes* button

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