FROM java:8
ENV DUBBO_IP_TO_REGISTRY 192.168.10.140
VOLUME /tmp
ADD user-app-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8086
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
