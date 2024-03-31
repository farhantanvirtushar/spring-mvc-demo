FROM openjdk:17


## Update the system
#RUN apt-get update
#
## Install Redis
#RUN apt-get install -y redis-tools

# Set the working directory inside the container
WORKDIR /app

# Copy the rest of the application code into the container
COPY target/spring-mvc-demo-0.0.1-SNAPSHOT.jar .

#RUN redis-server

EXPOSE 5000
EXPOSE 6379
#run command
CMD ["java", "-jar","spring-mvc-demo-0.0.1-SNAPSHOT.jar"]