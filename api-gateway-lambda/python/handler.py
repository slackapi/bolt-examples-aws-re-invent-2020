# ----------------------
# Using Docker for serverless deployments is a bit slow.
# This is a workaround for it.
# If you use another solution for deployments,
# you don't need to go with this way.
import sys
sys.path.insert(1, "lib/")
# ----------------------


import logging
from slack_bolt import App
from slack_bolt.adapter.aws_lambda import SlackRequestHandler


SlackRequestHandler.clear_all_log_handlers()
logging.basicConfig(format="%(asctime)s %(message)s", level=logging.DEBUG)


# process_before_response must be True when running on FaaS
app = App(process_before_response=True)


def respond_to_slack_within_3_seconds(event, logger):
    logger.info(f"Received: {event}")


def process_request(event, say):
    say(f"Hey there, <@{event['user']}>!")


app.event("app_mention")(
    ack=respond_to_slack_within_3_seconds,
    lazy=[process_request],
)


def slack_events(event, context):
    slack_handler = SlackRequestHandler(app=app)
    return slack_handler.handle(event, context)


if __name__ == "__main__":
    app.start(3000)
