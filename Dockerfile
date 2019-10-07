FROM openjdk:latest
COPY . /var/www/java
WORKDIR /var/www/java/src
RUN javac *.java
RUN echo Main-Class: RMIClient > MANIFEST.MF
RUN jar cfv RMIClient.jar MANIFEST.MF .
CMD ["java", "-jar", "RMIClient.jar"]
