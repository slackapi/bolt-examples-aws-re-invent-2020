package hello;

import com.slack.api.bolt.jetty.SlackAppServer;

public class JettyApp {
    public static void main(String[] args) throws Exception {
        var server = new SlackAppServer(SlackApp.get());
        server.start(); // POST https://localhost:3000/slack/events
    }
}