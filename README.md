
### Saving protobuf message in kafka and consuming from it using KafkaProtobuf serializer
    
1. Run docker-compose.yaml to start kafka and schema-registry locally
<code>docker-compose up</code>
2. Stop kafka once application is stopped by running following command at project root.
<code>docker-compose down --rmi 'all'</code>