package com.example.springreactive;

//import com.mongodb.MongoClient;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
//@EnableMongoRepositories(basePackages = "com.example.springreactive.data")
//public class MongoConfig extends AbstractMongoConfiguration {
//
//    @Override
//    protected String getDatabaseName() {
//        return "reactive";
//    }
//
//    @Override
//    public MongoClient mongoClient() {
//        return new MongoClient("127.0.0.1", 27017);
//    }
//}

@EnableReactiveMongoRepositories
class ApplicationConfig extends AbstractReactiveMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "reactive";
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }

}