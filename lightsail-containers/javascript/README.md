# Amazon Lightsail Containers ⚡️ Bolt for JavaScript

> Slack app example for deploying to Lightsail Containers with Bolt for JavaScript

## Overview

This is an example app that updates the [Getting Started ⚡️ Bolt for JavaScript app][bolt-app] to be deployed to [Amazon Lightsail Containers][aws-lightsail] using the [AWS CLI][aws-cli-install].

Before you begin, you may want to follow our [Getting Started guide][bolt-guide] to learn how to build your first Slack app using the [Bolt for JavaScript framework][bolt-website].

## Getting started

1. [Set up AWS credentials](#1-set-up-aws-credentials)
1. [Set up local project](#2-set-up-local-project)
1. [Create a Slack app](#3-create-a-slack-app)
1. [Deploy to Amazon Lightsail Containers](#4-deploy-to-aws-lightsail)
1. [Update Slack app settings](#5-update-slack-app-settings)
1. [Test your Slack app](#6-test-your-slack-app)
1. [Run on local machine](#7-run-on-local-machine)

## 1. Set up AWS credentials

### Install AWS CLI

Follow the guide to [install the AWS CLI for macOS, Windows, or Linux][aws-cli-install].

### Configure AWS CLI

Follow the guide to [create a new IAM User][aws-cli-configure-user] then configure your local machine with the command:

```zsh
aws configure
```

## 2. Set up local project

If you want to just build and deploy this app as-is, all you need is [Docker][docker].

For local development, you can install the app's dependencies with the following command:

```zsh
npm install
```

You may also want to [install ngrok][ngrok-install] to start a local tunnel for local development.

## 3. Create a Slack app

### Create an app on api.slack.com

1. Go to https://api.slack.com/apps
1. Select **Create New App**
    * Name your app, _don't worry you can change it later!_
1. Select **OAuth & Permissions**
    1. Scroll down to **Bot Token Scopes**
        1. Add the scope `app_mentions:read`
        1. Add the scope `chat:write`
    1. Select **Install App to Workspace** at the top of the page

### Export environment variables

```zsh
export SLACK_SIGNING_SECRET=<your-signing-secret> # Slack app settings > "Basic Information"
export SLACK_BOT_TOKEN=<your-xoxb-bot-token>      # Slack app settings > "OAuth & Permissions"
```

## 4. Deploy to Amazon Lightsail Containers

First off, let's create a service in Lightsail Containers:

```zsh
aws lightsail create-container-service \
  --service-name my-bolt-app \
  --power nano \
  --scale 1
```

Run the following the command to build a Docker image and publish it to the Lightsail Containers' registry:

```zsh
docker build . \
  -t my-bolt-app

aws lightsail push-container-image \
  --service-name my-bolt-app \
  --image my-bolt-app:latest \
  --label this-is-it
# ...
# Refer to this image as ":my-bolt-app.this-is-it.1" in deployments.
```

Please remember your image name (e.g. `:my-bolt-app.this-is-it.1`) because we will use it in an upcoming next.

You can check the current status at any time with the following commands:

```zsh
aws lightsail get-container-services
aws lightsail get-container-images --service-name my-bolt-app
```

For app deployment, we use a JSON file as input. You can generate it this way:

```zsh
# In the previous step, the `aws lightsail push-container-image` command returned this value to you.
# The value always starts with ":"
export LATEST_IMAGE_NAME=":my-bolt-app.this-is-it.{number}"

# Create `lc.json` file for the command we will run next
echo '{
  "serviceName": "my-bolt-app",
  "containers": {
    "my-bolt-app-container": {
      "image": "__LATEST_IMAGE_NAME__",
      "ports": {"80": "HTTP"},
      "environment": {
        "SLACK_BOT_TOKEN": "__SLACK_BOT_TOKEN__",
        "SLACK_SIGNING_SECRET": "__SLACK_SIGNING_SECRET__",
        "PORT": "80"
      }
    }
  },
  "publicEndpoint": {
    "containerName": "my-bolt-app-container",
    "containerPort": 80
  }
}' | \
  sed -e "s/__LATEST_IMAGE_NAME__/${LATEST_IMAGE_NAME}/" | \
  sed -e "s/__SLACK_BOT_TOKEN__/${SLACK_BOT_TOKEN}/" | \
  sed -e "s/__SLACK_SIGNING_SECRET__/${SLACK_SIGNING_SECRET}/" \
> lc.json
```

Let's deploy the image to the service:

```zsh
aws lightsail create-container-service-deployment \
  --cli-input-json file://lc.json
```

The deployment may take a bit. You can check the status by the `get-container-services` command:

```zsh
while true; do
  aws lightsail get-container-services \
  | jq '.containerServices[] | select(.containerServiceName == "my-bolt-app") | .state'
  sleep 5
done
```

Once your container service's state becomes **"RUNNING"** (not, **"READY"**), the app is ready for receiving requests from Slack. To know the public endpoint URL, run the following command:

```zsh
echo `aws lightsail get-container-services \
  | jq -r '.containerServices[] | select(.containerServiceName == "my-bolt-app") | .url'`slack/events
```

_Please note the endpoint `https://my-bolt-app.{random}.{region}.cs.amazonlightsail.com/slack/events` because we'll use it in the next section._

## 5. Update Slack app settings

Now that your Slack app is deployed, you can register your Amazon Lightsail endpoint with the Slack API:

1. Go to https://api.slack.com/apps
1. Select your app
1. Select **Event Subscriptions**
    1. Enable **Events**
    1. Set the **Request URL** to `https://my-bolt-app.{random}.{region}.cs.amazonlightsail.com/slack/events`
    1. Scroll down to **Subscribe to Bot Events**
    1. Add the following bot events:
        - `app_mention`
    1. Select **Save Changes**
1. Reinstall your app because permissions changed
    1. Select **OAuth & Permissions**
    1. Select **Reinstall to Workspace**

## 6. Test your Slack app

You can test your app by opening a Slack workspace and mentioning your app:

> 💬 Howdy @MySlackBot!
>
> 🤖 Hey there, @Jane!

_Remember, your app must be in the channel or DM where you mention it._

## 7. Run on local machine

Open a terminal session to listen for incoming requests:

```zsh
npm start
```

Open another terminal session to proxy Slack API requests locally:

```zsh
# -subdomain= is avalable only for paid accounts
ngrok http 3000 -subdomain=my-unique-name
```

Update your Slack app settings to use your ngrok address:
1. **Event Subscriptions**
    1. Set the **Request URL** to `https://my-unique-name.ngrok.io/slack/events`

Follow the steps to [test your app](#6-test-your-slack-app).

[aws-cli-install]: https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html
[aws-cli-configure]: https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html
[aws-cli-configure-user]: https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html#cli-configure-quickstart-creds
[aws-lightsail]: https://aws.amazon.com/lightsail/
[bolt-app]: https://github.com/slackapi/bolt-js-getting-started-app
[bolt-guide]: https://slack.dev/bolt-js/tutorial/getting-started
[bolt-website]: https://slack.dev/bolt-js/
[docker]: https://www.docker.com/
[ngrok-install]: https://api.slack.com/tutorials/tunneling-with-ngrok
[slack-app-settings]: https://api.slack.com/apps
