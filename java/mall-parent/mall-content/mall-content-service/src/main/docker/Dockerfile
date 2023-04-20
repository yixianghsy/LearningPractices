FROM java:8
VOLUME /tmp
ADD mall-content-service-1.0-SNAPSHOT.jar app.jar
# 设置环境变量
#ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
ENV DUBBO_IP_TO_REGISTRY 192.168.10.251
RUN echo "Asia/shanghai" > /etc/timezone
RUN bash -c 'touch /app.jar'
EXPOSE 8021

ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
