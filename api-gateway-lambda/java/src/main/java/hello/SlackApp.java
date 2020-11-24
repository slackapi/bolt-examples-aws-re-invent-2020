package hello;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.model.event.AppMentionEvent;

public class SlackApp {

    public static App get() {
        var config = new AppConfig();
        config.setSigningSecret(System.getenv(AppConfig.EnvVariableName.SLACK_SIGNING_SECRET));
        config.setSingleTeamBotToken(System.getenv(AppConfig.EnvVariableName.SLACK_BOT_TOKEN));
        var app = new App(config);
        app.event(AppMentionEvent.class, (req, ctx) -> {
            ctx.say(":wave: Hi there!");
            return ctx.ack();
        });
        return app;
    }

}