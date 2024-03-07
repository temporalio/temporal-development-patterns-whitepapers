package shoppingcartdemo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

public class Simple {
        public static void main(String[] args) throws Exception {
            // Create an ActorSystem
            ActorSystem system = ActorSystem.create("AskPatternExample");

            // Create an instance of the actor
            ActorRef myActor = system.actorOf(Props.create(MyActor.class), "myActor");

            // Use Timeout for the ask pattern
            Timeout timeout = Timeout.apply(5, TimeUnit.SECONDS);

            // Use the ask pattern to send a message to the actor and get a response
            Future<Object> future = Patterns.ask(myActor, "John", timeout);

            // Block and wait for the response
            String response = (String) Await.result(future, timeout.duration());

            // Print the response
            System.out.println("Response: " + response);

            // Terminate the ActorSystem
            system.terminate();
        }
    }
