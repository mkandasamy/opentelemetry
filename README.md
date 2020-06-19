# Opentelemetry Java Spring Boot Microservices

![opentelemetry](https://camo.githubusercontent.com/40361329470e9344e008d941d83809e9b947e4d4/68747470733a2f2f6f70656e74656c656d657472792e696f2f696d672f6c6f676f732f6f70656e74656c656d657472792d686f72697a6f6e74616c2d636f6c6f722e706e67?raw=true "Opentelemetry")

This maven project has 5 sample **spring boot** microservices backed by **elasticsearch** data store. All are exporting **opentelemetry** traces to **jaeger**. Metrics can be visualized in **prometheus** UI. A script to run **docker-compose** for these services is included in the project.

- product-service
- item-service
- review-service
- stock-service
- client-service
- elasticsearch
- elasticsearch db dump
- jaeger UI
- prometheus UI

## Steps to run

### Step 1 - install necessary tools

> Assuming your workstation station has **git**, **java 1.8^**, **maven**, **docker** and **docker-compose**
- install  [git](https://www.atlassian.com/git/tutorials/install-git)
- install [docker](https://docs.docker.com/get-docker/)
- install [docker compose](https://docs.docker.com/compose/install/)
- install [java](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
- install [maven](https://maven.apache.org/install.html)

### Step 2 - clone the repo
```console 
$ git clone https://github.com/mkandasamy/opentelemetry.git
```

### Step 3 - build the project

```console
$ cd opentelemetry

$ mvn clean install

```
![maven build](/screenshot/maven.png?raw=true "Maven Build")

### Step 4 - run the applications

```console
$ cd docker

$ chmod +x run.sh

$ ./run.sh -h 192.168.0.3 (***** replace this ip with your workstation private ip ******)
```

![containers](/screenshot/containers.png?raw=true "Containers")

### Step 5 - view trace
```console
http://localhost:16686/
```
![jaeger traces](/screenshot/jaeger-traces.png?raw=true "Jaeger Traces")

![jaeger spans](/screenshot/jaeger-spans.png?raw=true "Jaeger Spans")

### Step 6 - view metrics
```console
http://localhost:9090/
```
![prometheus](/screenshot/prometheus.png?raw=true "Prometheus")

### Step 7 - clean up
```console
$ docker kill $(docker ps -q)
$ docker rm $(docker ps -aq)
$ docker rmi $(docker images -aq)
```
