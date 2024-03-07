package shoppingcartdemo;

import akka.actor.*;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

// Define a simple actor
class MyActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    // Process the message and send a response
                    getSender().tell("Hello, " + message, getSelf());
                })
                .build();
    }
}


