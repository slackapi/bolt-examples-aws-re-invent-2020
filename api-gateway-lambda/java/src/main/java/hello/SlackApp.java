package hello;

import com.slack.api.bolt.App;
import com.slack.api.model.event.AppMentionEvent;

public class SlackApp {
    public static App get() {
        var app = new App();
        app.event(AppMentionEvent.class, (req, ctx) -> {
            var userId = req.getEvent().getUser();
            ctx.say("Hey there, <@" + userId + ">!");
            return ctx.ack();
        });
        return app;
    }
}