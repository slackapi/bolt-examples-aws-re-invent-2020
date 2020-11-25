# Deploying Bolt for Python apps to Lightsail Containers

# Create a new Slack app

Refer to [this guide](/README.md).

# How to deploy this app to AWS

## Deploy the app

```bash
# install aws-cli v2 https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-mac.html
aws configure

# Create a new Lightsail Container Service
# https://lightsail.aws.amazon.com/ls/webapp/home/containers
aws lightsail create-container-service --service-name my-bolt-app --power nano --scale 1

# Build a Docker image and push it to the registry
docker build . -t my-bolt-app
aws lightsail push-container-image --service-name my-bolt-app --image my-bolt-app:latest --label this-is-it

# Check the current state
aws lightsail get-container-services
aws lightsail get-container-images --service-name my-bolt-app

export SLACK_BOT_TOKEN=xoxb-
export SLACK_SIGNING_SECRET=

# Deploy the image

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

# Check the deployment status
aws lightsail get-container-service-deployments --service-name my-bolt-app
aws lightsail get-container-log --service-name my-bolt-app --container-name my-bolt-app
```

## Configure the *Request URL* for Events API

Refer to [this guide](/README.md). The **Request URL** should be `https://{your domain}.{your region}.cs.amazonlightsail.com/slack/events`.

# Local Development

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -U pip
pip install -r requirements.txt
export SLACK_SIGNING_SECRET=***
export SLACK_BOT_TOKEN=xoxb-***
FLASK_APP=main.py FLASK_ENV=development flask run -p 3000
# run `ngrok http 3000` in another terminal
```