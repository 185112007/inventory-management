# Inventory Management

A small inventory management microservice. This service should manage basic CRUD (Create, Read, Update, Delete) operations for inventory items.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need to install:

- JDK 17
- Docker 20.10.7

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Build projects

```
~$ unzip inventory-management.zip -d output/directory
~$ cd go/to/project/output/directory
~$ cd eureka-naming-server
~$ mvn clean install
~$ cd inventory-service
~$ mvn clean install
```

## Running

```
~$ cd eureka-naming-server
~$ ./mvnw spring-boot:run
~$ cd inventory-service
~$ ./mvnw spring-boot:run
```

## Authors

* **Gafur Hayytbayev**

See also the list of [contributors](https://github.com/185112007/inventory-management/contributors) who participated in this project.