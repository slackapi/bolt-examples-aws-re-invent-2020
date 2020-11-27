FROM gradle:6.5.1-jdk11 AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM gcr.io/distroless/java:11
COPY --from=build /home/gradle/src/build/libs /app
EXPOSE 80
ENTRYPOINT ["java",  "-jar", "/app/my-bolt-app.jar"]

#
# docker build . -t my-bolt-app
# docker run -e SLACK_SIGNING_SECRET=$SLACK_SIGNING_SECRET -e SLACK_BOT_TOKEN=$SLACK_BOT_TOKEN -p 80:80 -it my-bolt-app
#