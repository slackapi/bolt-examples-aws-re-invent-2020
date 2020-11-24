import sys
sys.path.insert(1, "lib/")

import logging
from slack_bolt import App
from slack_bolt.adapter.aws_lambda import SlackRequestHandler


SlackRequestHandler.clear_all_log_handlers()
logging.basicConfig(format="%(asctime)s %(message)s", level=logging.DEBUG)


# process_before_response must be True when running on FaaS
app = App(process_before_response=True)


def respond_to_slack_within_3_seconds(event, logger):
    logger.info(f"Received: {event}")


def process_request(say):
    say(":wave: Hi there!")


app.event("app_mention")(ack=respond_to_slack_within_3_seconds, lazy=[process_request])


def slack_events(event, context):
    slack_handler = SlackRequestHandler(app=app)
    return slack_handler.handle(event, context)


if __name__ == "__main__":
    # python3 -m venv .venv
    # source .venv/bin/activate
    # pip install -U pip
    # pip install -r requirements.txt
    # export SLACK_SIGNING_SECRET=***
    # export SLACK_BOT_TOKEN=xoxb-***
    # python handler.py
    app.start(3000)
