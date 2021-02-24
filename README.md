

# Restaurant Order Streaming service

##Quickstart

Start by [installing the Scala Build Tool, sbt](https://www.scala-sbt.org/1.x/docs/Setup.html).

You should also create an application.conf to start the process.
An example has been provided in this repository.

```
git clone https://github.com/greebie/restaurantStreaming.git
cd restaurantStreaming
sbt run
```

Using default values, your application should run via 
`http://localhost:8080/orders` where up to 100,000 restaurant 
orders will be produced at random for you.

## Dockerized version

This project can also build a Docker container.

```
git clone https://github.com/greebie/restaurantStreaming.git
cd restauranteStreaming
sbt docker:publishLocal
```

Will create a docker container on the local system. You can then run it using

```
docker run -it -p 8080:8080 restaurantstreaming
```