FROM adoptopenjdk/openjdk8-openj9:alpine-slim
#维护人员信息
MAINTAINER hsy
EXPOSE 8025
VOLUME /tmp
ADD mall-manager-web-1.0-SNAPSHOT.jar app.jar
RUN echo "Asia/shanghai" > /etc/timezone
RUN echo "Asia/shanghai" > /etc/timezone
#执行启动命令
ENTRYPOINT ["java", "-jar","/app.jar"]

# 设置环境变量
#ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
#ENV DUBBO_IP_TO_REGISTRY 192.168.10.140
# 时区设置

#RUN bash -c 'touch /app.jar'

#ENTRYPOINT ["java","exec java ${JAVA_OPTS}","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#执行启动命令
#ENTRYPOINT ["java", "-jar","/certif-system-2.1.0.jar"]
#ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
