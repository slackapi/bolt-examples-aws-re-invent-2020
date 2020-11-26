const { App, ExpressReceiver } = require('@slack/bolt');

// Create a Bolt Receiver
const receiver = new ExpressReceiver({ signingSecret: process.env.SLACK_SIGNING_SECRET });

// Initializes your app with your bot token and signing secret
const app = new App({
  token: process.env.SLACK_BOT_TOKEN,
  receiver
});

// Respond to an AWS health check
receiver.router.get('/', (req, res) => {
  res.status(200).send('ok');
});

// Listens for messages that mention the bot
app.event('app_mention', async ({ event, say }) => {
  await say(`Hey there, <@${event.user}>!`);
});

(async () => {
  // Start your app
  await app.start(process.env.PORT || 3000);

  console.log(`⚡️ Bolt app is running!`);
})();
