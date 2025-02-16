# Spring Kafka Debezium Example

This project demonstrates a simple Kafka setup integrated with Debezium for change data capture (CDC) from a MySQL database. The data is then processed by a Spring Boot application, which updates a Redis cache based on changes.

## Requirements

- Docker
- Docker Compose
- Redis
- Kafka
- MySQL
- Debezium Connector

## Services

The following services are set up using Docker Compose:

- **MySQL**: The source database where product data is stored.
- **Zookeeper**: Used by Kafka for coordination.
- **Kafka**: The message broker for stream processing.
- **Schema Registry**: Stores Avro schema for Kafka topics.
- **Kafka Connect (Debezium)**: Captures database changes and sends them to Kafka.
- **Redis**: Cache for storing product data.

## Running the Application

To run the application, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/ardasdasdas/spring-kafka-debezium-example.git
   cd spring-kafka-debezium-example

2. Build and start the services with Docker Compos
   ```bash
   docker-compose up --build
This will build and start the following services:
- MySQL on port 3306
- Kafka and Zookeeper on ports 9092 and 2181 respectively
- Redis on port 6379
- Schema Registry on port 8081
- Kafka Connect on port 8083
The Spring Boot application will be available on port 8080.

## Connecting MySQL to Kafka Connect

   ```bash
    curl --location 'http://localhost:8083/connectors' \
    --header 'Content-Type: application/json' \
    --data '{
        "name": "mysql-connector",
        "config": {
            "connector.class": "io.debezium.connector.mysql.MySqlConnector",
            "tasks.max": "1",
            "database.hostname": "mysql",
            "database.port": "3306",
            "database.user": "root",
            "database.password": "root",
            "database.server.id": "184054",
            "database.server.name": "dbserver",
            "database.include.list": "testdb",
            "table.include.list": "testdb.products",
            "database.history.kafka.bootstrap.servers": "kafka:9092",
            "database.history.kafka.topic": "dbhistory.fullfillment",
            "include.delete": "true"
        }
    }'
```
This will set up the Debezium MySQL connector.

## Example Requests

1. Save a Product
To save a new product, make a POST request to the /products endpoint:
```bash
curl --location --request POST 'http://localhost:8080/products' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Product Name",
    "price": 19.99
}'
```
2. Update a Product
To update an existing product, make a PUT request to the /products endpoint:
```bash
curl --location --request PUT 'http://localhost:8080/products' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "name": "Updated Product Name",
    "price": 24.99
}'
```
3. Delete a Product
To delete a product by its ID, make a DELETE request:
```bash
curl --location --request DELETE 'http://localhost:8080/products/1'
```

## Docker Redis Commands

To get the value of a product from Redis:
```bash
docker exec -it <redis_container_name> redis-cli GET product:<product_id>
```
Replace <redis_container_name> with the actual container name for Redis, and <product_id> with the product ID you want to fetch.




