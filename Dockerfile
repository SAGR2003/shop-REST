FROM openjdk:19-alpine

COPY build/libs/shop-REST-0.0.1-SNAPSHOT.jar /app/shop-rest.jar

WORKDIR /app

EXPOSE 8081

CMD ["java", "-jar", "shop-rest.jar", "--server.port=8081"]