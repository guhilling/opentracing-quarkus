## Demo for using opentracing/jaeger with quarkus

The project files contain two [Quarkus](https://quarkus.io) projects. Each will provide a REST-style API.
Informations about requests will be sent to the all-in-one jaeger server started by docker-compose.yml.

## Instruction to get started

As I also host the docker images on Dockerhub you don't _have_ to build the code yourself. You can just start
with `docker-compose up` and test the services.
The containers will try to open the following ports on the host:
- 8080 (quarkus-hello)
- 8081 (quarkus-world)
- 5432 (postgresql)
- 16686 (jaeger-ui)

For testing only 8081 and 16686 are actually required.

## Provided services

To trigger creation of traces in jaeger you can use the following url (via `curl`):
- [http://localhost:8080/hello/name](http://localhost:8080/hello/sam) (return `<name>`)
- [http://localhost:8080/hello/world](http://localhost:8080/hello/world) (will trigger second service)
- [http://localhost:8081/world](http://localhost:8081/world) (direct call of second service)
- [http://localhost:8080/demo](http://localhost:8080/demo) (demo of using the api directly)

## See results

The resulting traces can be inspected via the jaeger ui running on [http://localhost:16686](http://localhost:16686)

