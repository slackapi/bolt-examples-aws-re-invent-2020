FROM python:3.8.5-slim-buster as builder
COPY requirements.txt /build/
WORKDIR /build/
RUN pip install -U pip && pip install -r requirements.txt

FROM python:3.8.5-slim-buster as app
WORKDIR /app/
COPY --from=builder /usr/local/bin/ /usr/local/bin/
COPY --from=builder /usr/local/lib/ /usr/local/lib/
COPY *.py /app/
EXPOSE 80
ENTRYPOINT gunicorn --bind :80 --workers 1 --threads 2 --timeout 0 main:flask_app

#
# docker build . -t my-bolt-app
# docker run -e SLACK_SIGNING_SECRET=$SLACK_SIGNING_SECRET -e SLACK_BOT_TOKEN=$SLACK_BOT_TOKEN -p 80:80 -it my-bolt-app:latest
#
