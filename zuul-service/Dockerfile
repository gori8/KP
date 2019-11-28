FROM hirokimatsumoto/alpine-openjdk-11
ADD target/zuul-service-0.0.1-SNAPSHOT.jar zuul.jar
ENTRYPOINT ["java","-jar","/zuul.jar"]
EXPOSE 8771
