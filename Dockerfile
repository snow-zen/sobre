FROM openjdk:8-jre-slim as builder

WORKDIR /application

ARG TODO_JAR=./todo/target/todo-0.0.1-SNAPSHOT.jar

COPY ${TODO_JAR} application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:8-jre-slim

WORKDIR /application

COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]