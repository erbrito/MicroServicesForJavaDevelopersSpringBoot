This project is based on the book:

"Microservices for java developers"

spring init --build maven --groupId com.redhat.examples --version 1.0 --java-version 1.8 --dependencies web --name hola-springboot hola-springboot

enabled those endpoints:

* http://localhost:8080/api/hola
* http://localhost:8080/metrics
* http://localhost:8080/beans
* http://localhost:8080/env
* http://localhost:8080/health
* http://localhost:8080/trace
* http://localhost:8080/mappings

to run it:

$ mvn clean install spring-boot:run -Dserver.port=9090