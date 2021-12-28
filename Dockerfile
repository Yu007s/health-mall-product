FROM harbor.leke.cn/basic/openjdk:8-jdk-alpine as builder
ARG APP_NAME
MAINTAINER wangfeng1 "wangfeng1@cnstrong.com"
WORKDIR application
COPY ${APP_NAME}-webapp/target/${APP_NAME}.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM harbor.leke.cn/basic/openjdk:8-jdk-alpine
MAINTAINER wangfeng1 "wangfeng1@cnstrong.com"
WORKDIR application
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/strong-lib-dependencies/ ./
COPY --from=builder /application/strong-app-dependencies/ ./
COPY --from=builder /application/application/ ./
ADD bin/docker-startup.sh bin/startup.sh

ENV JVM_OPTS '-Xmx256m -Xms64m -Xss256k'
ENV JVM_AGENT ''
ENV SERVER_PORT 8080
EXPOSE ${SERVER_PORT}

RUN mkdir logs \
    && cd logs \
    && touch start.log \
    && ln -sf /dev/stdout start.log \
    && ln -sf /dev/stderr start.log

RUN chmod +x bin/startup.sh
ENTRYPOINT [ "sh", "-c", "sh bin/startup.sh"]

