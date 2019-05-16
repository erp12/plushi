FROM java:8-alpine
RUN mkdir -p /app /app/resources
WORKDIR /app
COPY target/uberjar/*-standalone.jar .
EXPOSE 8075
CMD java -jar $(ls plushi*SNAPSHOT-standalone.jar) --start
