# bolt-examples-aws-re-invent-2020

This repository is a collection of Slack app examples built with [Bolt](https://api.slack.com/tools/bolt) for AWS re:Invent 2020 visitors.

# Create a Slack App

## Create and enable a Slack app

* Go to https://api.slack.com/apps
* Create a new Slack app
* Go to **Features** > **OAuth & Permissions**
* Add `app_mentions:read` to **Scopes** > **Bot Token Scopes**
* Add `chat:write` to **Scopes** > **Bot Token Scopes**
* Go to **Settings** > **Install App**
* Click **Install to Workspace**
* Complete the installation process

## Grab the credentials

* SLACK_SIGNING_SECRET: **Settings** > **Basic Information** > **App Credentials** > **Signing Secret**
* SLACK_BOT_TOKEN: **Settings** > **Install App** > **OAuth Tokens for Your Team** > **Bot User OAuth Access Token**

## Configure the *Request URL* for Events API

Follow this step after you've got a public URL to send requests from Slack.

* Go to https://api.slack.com/apps and choose your app
* Go to **Features** > **Event Subscriptions**
* Turn the feature on
* Set **Request URL**
* Click **Subscribe to bot events**
* Add `app_mention` event
* Click **Save Changes** button
