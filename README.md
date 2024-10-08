# book-service

## Description

_The project is a task according to [assignment](./assignment/README.md)._

RESTful API that provides the following operations:

- Get a word count of a sentence by its ID
- Get a list of sentences grouped by book IDs
- Insert a book with a list of sentences into the database
- Insert single a sentence into the database

A detailed description of endpoints can be found in swagger.

http://localhost:8080/dot-interview/swagger-ui/index.html#/

*\*use your own server port and context path in case of you changed it*

## Build

### Requirements:

1. Java 11
2. Gradle 7.x
3. Docker (optional, for docker build)

#### local build:

`gradle clean bootJar`

`java -jar ./build/libs/dot-interview-kotlin-0.0.1-SNAPSHOT.jar`

#### docker build:

`docker build -t book-service .`

`docker run -dp 8080:8080 book-service`

#### Environmental variables:

| env           | default        | required | description                     |
|---------------|----------------|----------|---------------------------------|
| SERVER_PORT   | 8080           | no       | Server port                     |
| CONTEXT_PATH  | /dot-interview | no       | Additional Uri for app          |
| DB_URL        | jdbc:h2:mem:db | no       | H2 database URL                 |
| DB_USERNAME   | inno           | no       | Username for H2 database access |
| DB_PASSWORD   | innopass       | no       | Password for H2 database access |
| LOGGING_LEVEL | INFO           | no       | Root logger level               |

#### Variables using:

##### local build:

Variables can be passed via CMD during program starting with flag `-D`, f. e.:

`java -jar -DLOGGING_LEVEL=DEBUG java -jar ./build/libs/dot-interview-kotlin-0.0.1-SNAPSHOT.jar`

or

Variables can be configured directly in `src/main/resources/application.yml`. Do not recommended method.

##### docker build:

Variables can be configured directly in `Dockerfile` in block ENV, f.e.:

`ENV SERVER_PORT=8080`

or

Variables can be passed via CMD during container starting with flag `--env`, f. e.:

`docker run -dp 8080:8080 --env LOGGING_LEVEL=DEBUG book-service`

## Monitoring API:
*\*use your own server port and context path in case of you changed it*

| url                                                         | description        |
|-------------------------------------------------------------|--------------------|
| http://localhost:8080/dot-interview/swagger-ui/index.html#/ | Swagger UI         |
| http://localhost:8080/dot-interview/actuator/               | Actuator           |
| http://localhost:8080/dot-interview/actuator/health         | Health check       |
| http://localhost:8080/dot-interview/actuator/prometheus     | Prometheus metrics |
| http://localhost:8080/dot-interview/h2-console/             | H2 web console     |


#### Additional info
*\*Project uses detekt for code style checking and ktlint for code formatting.*
