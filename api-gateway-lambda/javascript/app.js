const { App, ExpressReceiver } = require('@slack/bolt');
const awsServerlessExpress = require('aws-serverless-express');

// Initialize your custom receiver
const expressReceiver = new ExpressReceiver({
  signingSecret: process.env.SLACK_SIGNING_SECRET,
  processBeforeResponse: true
});

// Initializes your app with your bot token and signing secret
const app = new App({
  token: process.env.SLACK_BOT_TOKEN,
  receiver: expressReceiver
});

// Initialize your AWSServerlessExpress server using Bolt's ExpressReceiver
const server = awsServerlessExpress.createServer(expressReceiver.app);

// Listens for messages that mention the bot
app.event('app_mention', async ({ event, say }) => {
  await say(`Hey there, <@${event.user}>!`);
});

// Handle the function event
module.exports.handler = (event, context) => {
  console.log('⚡️ Bolt app is running!');
  awsServerlessExpress.proxy(server, event, context);
};