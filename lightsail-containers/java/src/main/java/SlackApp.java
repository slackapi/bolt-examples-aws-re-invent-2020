import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.event.AppMentionEvent;

public class SlackApp {
    public static void main(String[] args) throws Exception {
        var app = new App();
        app.event(AppMentionEvent.class, (req, ctx) -> {
            var userId = req.getEvent().getUser();
            ctx.say("Hey there, <@" + userId + ">!");
            return ctx.ack();
        });
        app.endpoint("/", (req ,ctx) -> ctx.ack()); // for AWS health check
        var server = new SlackAppServer(app, 80);
        server.start();
    }
}
