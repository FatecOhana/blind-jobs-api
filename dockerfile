FROM amazoncorretto:17

WORKDIR /app

COPY target/blind-jobs-0.0.1-SNAPSHOT.jar /app/spring-app.jar

# ARG JAR_FILE=build/libs/*SNAPSHOT.jar
# COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/spring-app.jar"]