FROM eclipse-temurin:17

LABEL mentainer="ydiwakarrd@gmail.com"

WORKDIR /app

COPY target/springboot-blog-rest-0.0.1-SNAPSHOT.jar /app/springboot-blog-rest-docker.jar

ENTRYPOINT [ "java", "-jar", "springboot-blog-rest-docker.jar" ]

