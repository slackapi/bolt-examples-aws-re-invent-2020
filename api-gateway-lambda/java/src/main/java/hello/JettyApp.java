package hello;

import com.slack.api.bolt.jetty.SlackAppServer;

public class JettyApp {

    // export SLACK_SIGNING_SECRET=
    // export SLACK_BOT_TOKEN=xoxb-
    // gradle run
    public static void main(String[] args) throws Exception {
        var server = new SlackAppServer(SlackApp.get());
        server.start(); // POST https://localhost:3000/slack/events
    }
}