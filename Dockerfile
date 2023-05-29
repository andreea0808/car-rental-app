FROM openjdk:17-jdk

WORKDIR /app

COPY target/car-rental-app-1.0.0.jar /app/car-rental-app.jar

EXPOSE 8080

CMD ["java", "-jar", "car-rental-app.jar"]
