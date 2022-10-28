FROM java:8
#FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD mall-manager-service-1.0-SNAPSHOT.jar app.jar
ENV DUBBO_IP_TO_REGISTRY 192.168.10.140
# 设置环境变量
ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
# 时区设置
RUN echo "Asia/shanghai" > /etc/timezone
RUN bash -c 'touch /app.jar'
EXPOSE 8024
ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]