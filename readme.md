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

Fabric8 Support
--------------

Install forge:

$ forge
$ addon-install --coordinate io.fabric8.forge:devops,2.2.153



OpenShift:
----------


$ oc cluster up --public-hostname='127.0.0.1' --host-data-dir=/Users/ebrito/oc/profiles/default/data --host-config-dir=/Users/ebrito//oc/default/config --image=registry.access.redhat.com/openshift3/ose --version=v3.4.1.10-3
$ oc new-project microservices-book

$ mvn -Pf8-build
$ mvn -Pf8-local-deploy

$ oc get pod
NAME                    READY     STATUS    RESTARTS   AGE
hola-springboot-0cipr   1/1       Running   0          3m


$ oc get pod
NAME                    READY     STATUS    RESTARTS   AGE
hola-springboot-0cipr   1/1       Running   0          3m
ebrito:Downloads ebrito$ oc get rc
NAME              DESIRED   CURRENT   READY     AGE
hola-springboot   1         1         1         3m
ebrito:Downloads ebrito$ oc scale rc hola-springboot --replicas=3
replicationcontroller "hola-springboot" scaled
ebrito:Downloads ebrito$ oc get rc
NAME              DESIRED   CURRENT   READY     AGE
hola-springboot   3         3         1         4m
ebrito:Downloads ebrito$ oc get pod
NAME                    READY     STATUS    RESTARTS   AGE
hola-springboot-0cipr   1/1       Running   0          4m
hola-springboot-17e5n   1/1       Running   0          47s
hola-springboot-1hqox   1/1       Running   0          47s






