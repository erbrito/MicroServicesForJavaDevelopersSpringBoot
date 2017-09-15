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


S2I, creating a new application
-------------------------------

$ oc new-app codecentric/springboot-maven3-centos~https://github.com/erbrito/MicroServicesForJavaDevelopersSpringBoot.git --name=holamicro


### update configruation of pods with config maps:

#### create configmap
$ oc create configmap hola-config --from-file=src/main/openshift/application.properties

### Update deployment confituration to use the configmap

$ oc get dc
NAME        REVISION   DESIRED   CURRENT   TRIGGERED BY
holamicro   10         1         1         config,image(holamicro:latest)


$ oc describe dc holamicro

Containers:
    Volume Mounts:
      /opt/app-root/src/config from volume-z8608 (rw)

Volumes:
   volume-z8608:
    Type:       ConfigMap (a volume populated by a ConfigMap)
    Name:       hola-config


Strategy:
use the web console the first time to get it done, or use the template file from any source of inspiration
then, export it with:

 $ oc export dc holamicro -o yaml > src/main/openshift/dc.yml

 all the configurations can be found at src/main/openshift/rawTemplate.yml


### adding health checks:

Liveness:   http-get http://:8080/api/hola delay=10s timeout=1s period=10s #success=1 #failure=3
Readiness:  http-get http://:8080/configprops delay=5s timeout=1s period=10s #success=1 #failure=3

### Notes to be automated
1) we can change the builder images with the official Red Hat images.

oc cluster up --host-data-dir=/Users/ebrito/oc/profiles/default/data-opticca --host-config-dir=/Users/ebrito/oc/default/config-opticca

oc new-project microservices-bestpractices-dev

oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/erbrito/MicroServicesForJavaDevelopersSpringBoot.git --name=exposed-springboot-microservice

oc expose service/backend-microservice --port=8080


oc new-app  registry.access.redhat.com/jboss-eap-7/eap70-openshift~https://github.com/erbrito/MicroServicesForJavaDevelopersBackend.git --name=backend-microservice

oc get services

oc expose service/exposed-springboot-microservice --port=8080


oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/erbrito/MicroServicesForJavaDevelopersSpringBoot.git --name=exposed-springboot-microservice-enhanced

oc new-app codecentric/springboot-maven3-centos~https://github.com/erbrito/MicroServicesForJavaDevelopersSpringBoot.git --name=exposed-springboot-microservice-enhanced2

oc expose service/exposed-springboot-microservice-enhanced --port=8080 --hostname=app1.127.0.0.1.nip.io

oc create configmap hola-config --from-file=./application.properties

oc set volume dc/exposed-springboot-microservice-enhanced --add -m /opt/app-root/src/config --configmap-name=hola-config --name=configuration-volume


http://app1.127.0.0.1.nip.io/api/hola

http://app1.127.0.0.1.nip.io/api/greeting

http://app1.127.0.0.1.nip.io/api/greeting-circuit-breaker



Logs from base image registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:
Starting the Java application using /opt/run-java/run-java.sh ...
exec java -javaagent:/opt/jolokia/jolokia.jar=config=/opt/jolokia/etc/jolokia.properties -XX:+UseParallelGC -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:MaxMetaspaceSize=100m -XX:+ExitOnOutOfMemoryError -cp . -jar /deployments/hola-springboot-1.0.jar
