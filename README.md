# kafkaconsumer
This is a Kafka Consumer microservice.  When invoked with GET request with endpoint http://localhost:8080/consumer, this microservice will poll the 
kafka topic for 10 seconds and returns the messages back to the caller.
