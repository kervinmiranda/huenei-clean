# Google Cloud Platform openjdk 8 Docker Image
FROM gcr.io/google-appengine/openjdk:8

# Default to UTF-8 file.encoding
ENV LANG="C.UTF-8"\
   JAVA_OPTS="-Xms128m -Xmx256m -XX:PermSize=128M -XX:MaxNewSize=256m -XX:MaxPermSize=256m"

# Default copy (Gradle)
COPY build/libs/huenei-clean.jar /api/app.jar

# Default workspace dir
RUN ls /api
WORKDIR /api

ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar app.jar
