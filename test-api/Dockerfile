FROM hirokimatsumoto/alpine-openjdk-11
ADD target/test-api-0.0.1-SNAPSHOT.jar test-api.jar
ENTRYPOINT ["java","-jar","/test-api.jar"]
EXPOSE 8088
