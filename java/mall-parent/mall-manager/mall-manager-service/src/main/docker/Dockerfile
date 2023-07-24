#设置镜像基础，jdk8
FROM openjdk:8
#维护人员信息
MAINTAINER hsy
#设置镜像对外暴露端口
EXPOSE 8024
#将当前 target 目录下的 jar 放置在根目录下，命名为 app.jar，推荐使用绝对路径。
VOLUME /tmp
ADD mall-manager-service-0.0.1-SNAPSHOT.jar app.jar
# 设置环境变量
#ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
# set  部署服务器IP
#ENV DUBBO_IP_TO_REGISTRY 192.168.10.251
# 时区设置
RUN echo "Asia/shanghai" > /etc/timezone
#执行启动命令
ENTRYPOINT ["java", "-jar","/app.jar"]
#ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /certif-system-2.1.0.jar
#ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /certif-system-2.1.0.jar
#ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /app.jar