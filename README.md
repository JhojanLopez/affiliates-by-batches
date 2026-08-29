# Affiliates Batch Processor

Spring Boot application that uses Spring Batch to process large volumes of affiliate records from a CSV file and store them in a PostgreSQL database.
Built with Spring Boot 3 and Java 17, this project is designed to efficiently handle bulk data ingestion for huge numbers of affiliates.

## Features

- Reads affiliate data from a CSV file using Spring Batch's chunk-oriented processing
- Validates and transforms records before persisting them
- Writes processed records to a PostgreSQL database
- Handles large datasets efficiently through batched reads/writes and configurable chunk size
- Job execution tracking via Spring Batch's metadata tables

## Requirements

- Java 17 is required
- PostgreSQL database deployed

## Deployment

You can run the application in one of two ways:

- With Gradle (no installation required):

1. Run the following command in the root of the project. You don't need to compile the source code or have Gradle installed:
```shell
./gradlew bootRun
```
