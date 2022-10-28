FROM java:8
VOLUME /tmp
ADD mall-content-service-0.0.1-SNAPSHOT.jar app.jar
# 设置环境变量
ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
ENV DUBBO_IP_TO_REGISTRY 192.168.10.140
RUN bash -c 'touch /app.jar'
EXPOSE 8021
#ENTRYPOINT ["java","exec java ${JAVA_OPTS}","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#执行启动命令
#ENTRYPOINT ["java", "-jar","/certif-system-2.1.0.jar"]
ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
#设置基础镜像jdk8
#FROM java:8
##维护人员信息
#MAINTAINER HSY
##设置镜像对外暴露端口
#EXPOSE 8021
##将当前 target 目录下的 jar 放置在根目录下，命名为 app.jar，推荐使用绝对路径。
#ADD mall-content-service-0.0.1-SNAPSHOT.jar app.jar
## 设置环境变量
#ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
## 时区设置
#RUN echo "Asia/shanghai" > /etc/timezone
##执行启动命令
##ENTRYPOINT ["java", "-jar","/certif-system-2.1.0.jar"]
#ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
