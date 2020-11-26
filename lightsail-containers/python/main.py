import logging

from slack_bolt import App

logging.basicConfig(level=logging.DEBUG)
app = App()


@app.event("app_mention")
def hello(event, say):
    say(f"Hey there, <@{event['user']}>!")


from flask import Flask, request
from slack_bolt.adapter.flask import SlackRequestHandler

flask_app = Flask(__name__)
handler = SlackRequestHandler(app)

@flask_app.route("/", methods=["GET"])
def healthcheck():
    return "OK"


@flask_app.route("/slack/events", methods=["POST"])
def slack_events():
    return handler.handle(request)
