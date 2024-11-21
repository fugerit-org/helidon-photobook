# Micronaut Photobook Demo App

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](https://github.com/fugerit-org/helidon-photobook/blob/master/CHANGELOG.md)
[![license](https://img.shields.io/badge/License-Apache%20License%202.0-teal.svg)](https://opensource.org/licenses/Apache-2.0)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_helidon-photobook&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit-org_helidon-photobook)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_helidon-photobook&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit-org_helidon-photobook)
[![Docker images](https://img.shields.io/badge/dockerhub-images-important.svg?logo=Docker)](https://hub.docker.com/repository/docker/fugeritorg/helidon-photobook/general)

## Introduction

Recently I followed some [Mongo DB courses](https://learn.mongodb.com/) and attended the [Spring I/O 2023](https://2023.springio.net/).  
So I decided to practice a bit. This project is the result.  
Currently is just a simple POC integration of Mongo DB and Micronaut

## Prerequisites

| software                                                                                                                                                                                                 | docker compose | local build and run |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------|---------------------|
| [![Java runtime version](https://img.shields.io/badge/run%20on-java%2021+-%23113366.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java21.html)   | no             | yes                 |
| [![Java build version](https://img.shields.io/badge/build%20on-GraalVM%2021+-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/gvm21.html) | no             | yes                 |
| [![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9.0+-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://universe.fugerit.org/src/docs/versions/maven3_9.html)       | no             | yes                 |
| [![Node JS](https://img.shields.io/badge/Node%20JS-20+-1AC736?style=for-the-badge&logo=node.js&logoColor=white)](https://universe.fugerit.org/src/docs/versions/node.html)                               | no             | yes                 |
| [![Docker](https://img.shields.io/badge/docker-26+-1266E7?style=for-the-badge&logo=docker&logoColor=white)](https://universe.fugerit.org/src/docs/versions/docker.html)                                  | yes            | no                  |

## Quickstart

### Start via docker compose

```shell
docker-compose -f src/main/docker/docker-compose.yml up -d
```

### Start in dev mode

1. Create mongo db instance with db initialization (script src/test/resources/mongo-db/mongo-init.js) :

```shell
docker run --rm -p 27017:27017 --name MONGO8 -v `pwd`/src/test/resources/mongo-db/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js mongo:8.0.3
```

This will start a mongo db linked on the default port and with the default username/password (root/example).

2. Start the application in dev mode

The back end

If the [Helidon CLI](https://helidon.io/docs/v4/about/cli) is installed, just run : 

```shell
mvn mn:run
```

And front end

```shell
cd src/main/react
npm install
npm run start
```

3. Access home page

<http://localhost:8080/photobook-demo/home/index.html>

## project environment variables

In case you want a custom mongo db connection :

| key         | dedault                                  | 
|-------------|------------------------------------------|
| MONGODB_URL | mongodb://localhost:27017/photobook_demo |

## container environment variables

Other custom environment variables :

| key              | dedault | description                                 |
|------------------|---------|---------------------------------------------|
| JAVA_OPTS_APPEND |         | append java options (for instance '-Xmx1g') |

## Helidon package

It is possible to compile the application to a single jar package :

```shell
mvn package -Pbuildreact
```

And then run

```shell
java -jar target/helidon-photobook.jar
```

## Native image compilation

The code has been set for native compilation with [GraalVM](https://www.graalvm.org/) (tested with GraalVM 22.3 CE).  

Here is the relevant [Helidon MP - GraalVM Native Image Guide](https://helidon.io/docs/v4/mp/guides/graalnative).

It is possible to compile :

```shell
mvn -Pnative-image install -Pbuildreact
```

And then run

```shell
./target/helidon-photobook
```

## Helidon documentation

Refer to [Helidon Documentation](https://helidon.io/docs/v4/about/doc_overview) for more information on Helidon.

## Docker image

As stated in the guide micronaut guide : 
<https://guides.micronaut.io/latest/micronaut-docker-image-maven-java.html>

It is possible to use a command as simple as  

```shell    
mvn package -Dpackaging=docker
```

But here I prefer to use classical Dockerfile for setup of github workflow and docker hub push.

### docker container (jvm)

Build micronaut application (jar)

```shell
mvn package -Pbuildreact
```

Build container openjdk

```shell
docker build -t helidon-photobook-jvm -f src/main/docker/Dockerfile.jvm .
```

Running the container :

```shell
docker run -it -p 8080:8080 --name helidon-photobook-jvm helidon-photobook-jvm
```

### docker container (native)

Building the native image :

```shell
mvn package -Dpackaging=native-image -Pbuildreact
```

Building the container image : 

```shell
docker build -t helidon-photobook-native -f src/main/docker/Dockerfile.native .
```

Running the container :

```shell
docker run -it -p 8080:8080 --name helidon-photobook-native helidon-photobook-native
```

## Native optimization : PGO

This section is based on <https://github.com/alina-yur/native-spring-boot>.

One of the most powerful performance optimizations in Native Image is profile-guided optimizations (PGO).

1. Build an instrumented image:

```shell
mvn package -Dpackaging=native-image -Pinstrumented
```

2. Run the app and apply relevant workload:

```shell
./target/helidon-photobook-instrumented
```

```shell
hey -n=30000 http://localhost:8080/photobook-demo/api/photobook/view/list
```

```shell
hey -n=30000 http://localhost:8080/photobook-demo/api/photobook/view/images/springio23/language/it/current_page/1/page_size/5
```

after you shut down the app, you'll see an `iprof` file in your working directory.

3. Build an app with profiles (they are being picked up via `<buildArg>--pgo=${project.basedir}/default.iprof</buildArg>`):

```shell
mvn package -Dpackaging=native-image -Poptimized
```

## Benchmark scripts

Prerequisites :
- running mongo db
- `hey` installed
- `psrecord` installed

At the end of a run, an image will be plotted in the `target` folder.

### 1. Benchmark JIT

```shell
mvn clean package
```

```shell
./src/main/script/bench-jit.sh
```

### 2. Benchmark native

Follow steps in 'Native optimization : PGO' section

```shell
./src/main/script/bench-native.sh
```

### 3. Benchmark result

Here are shown the result of a run on one of my systems (Multiple runs have been recorded with consistent results) : 


```
Ubuntu 22.04.4 LTS
AMD Ryzen 7 3700X
32 GB of RAM
```

Sample result of JIT benchmark run :

![JIT Benchmark Result](src/main/docs/images/bench-result-jit-20240619.png)

Sample result of native (AOT) benchmark run :

![Native Benchmark Result](src/main/docs/images/bench-result-native-20240619.png)

## application stack

| Layer             | 2024 version    |
|-------------------|-----------------|
| Persistence       | MongoDB 8       |
| Java version      | GraalVM 21      |
| API REST          | Micronaut 4.5.x |
| Node JS           | Node 20         |
| Front end package | Vite            |
| Front end UI      | React 18.3      |

## Deploy on Google App Engine

Prerequisites :
* google cloud account
* GCloud CLI

[Simple app.yaml for AOT](src/main/appengine/app-aot.yaml) and
[Simple app.yaml for JIT](src/main/appengine/app-jit.yaml)

Customize the app.yaml and run gcloud

```shell
gcloud helidon-photobook-*runner.jar --appyaml=src/main/appengine/app-jit.yaml
```

For JIT version or

```shell
gcloud helidon-photobook --appyaml=src/main/appengine/app-aot.yaml
```

For AOT version

Further reference :
* <https://cloud.google.com/sdk/gcloud/reference/app/deploy>

## Deploy on KNative

Prerequisites :
* container environment (docker / podman)
* Kubernates (for instance minikube)
* Knative

After setting up Knative,
Customize the [helidon-photobook-jit.yaml](src/main/knative/helidon-photobook-jit.yaml) or
[helidon-photobook-aot.yaml](src/main/knative/helidon-photobook-aot.yaml) service deployment.

And run

```shell
kubectl apply -f helidon-photobook-jit.yaml
```

For JIT version or

```shell
kubectl apply -f helidon-photobook-aot.yaml
```

For AOT version