FROM openjdk:latest
COPY . /var/www/java
WORKDIR /var/www/java/src
RUN javac *.java
RUN jar cfv RMIClient.jar RMIClient RMIClient.class
CMD ["java", "-jar", "RMIClient.jar"]
