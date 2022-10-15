FROM java:8
VOLUME /tmp
##复制jar并改名app.jar
ADD *.jar app.jar
##复制文件到目录
COPY startup.sh /startup.sh
## ENV设置环境变量
ENV TZ=Asia/Shanghai
##RUN执行命令
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN bash -c 'touch /app.jar'
##容器启动命令,这里是运行startup.sh,等待mysql启动后启动容器
CMD ["sh", "/startup.sh"]