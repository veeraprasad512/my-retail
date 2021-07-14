# my-retail

This application was generated using Spring Initializer, you can find documentation and help at [SpringBoot Docs][].

This is a "microservice" application intended to be part of a microservice architecture, please refer to the [Doing microservices with SpringBoot][] page of the documentation for more information.

This microservice (my-retail) is configured to run on port 8500. This can be reconfigured in the application.properties file.

This microservice (my-retail) uses embedded mongodb as backend database which is not persistent. As you stop your application all the data you write to it will be lost. This is purely for development purposes only. If you want to use your local mongo db instance, just update the scope of embedded-mongo to <scope>test</scope> so that embedded mongo will only be used during test phase.

For development purposes, sample data is being inserted into embedded mongo from the repository layer after @PostConstruct of the bean.

## Development

To start your application in the default profile, run:

```
./mvnw
```

For further instructions on how to develop with Spring Boot, have a look at [Using SpringBoot in development][].

## Building for production

### Packaging as jar

To build the final jar and optimize the my-retail application for production, run:

```
./mvnw -Pprod clean verify
```

To ensure everything worked, run:

```
java -jar target/*.jar
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Installing your application locally

To install your application in your local environment, run:

```
./mvnw install
```

For more information, refer to the [Running tests page][].

[Doing microservices with SpringBoot]: https://spring.io/microservices/
[Using SpringBoot in development]: https://spring.io/projects/spring-boot
[SpringBoot Docs]: https://docs.spring.io/initializr/docs/current/reference/html/
