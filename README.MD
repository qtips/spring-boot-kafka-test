This project involves Kafka consumer and producer, and Postgresql database and a rest-service

The tests demonstrate:
1. Calling a rest service that adds a message to a (embedded) Kafka topic, where the message consumer writes to a postgresql database
2. Multithreaded usage of postgresql with Spring Data and JPA and locking of rows. 

# TODO: 
Simulate Kafka calls that result in errors in multithreaded environment to test different kinds of kafka settings and guarentees (e.g. exactly once vs at least once processing)