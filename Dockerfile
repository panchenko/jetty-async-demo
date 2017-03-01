FROM java:latest
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

ADD target/async-1.0-SNAPSHOT.jar /data/
EXPOSE 8080
CMD ["java", "-cp", "/data/async-1.0-SNAPSHOT.jar", "com.ninecookies.async.App"]
