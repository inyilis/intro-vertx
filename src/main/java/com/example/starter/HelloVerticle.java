package com.example.starter;

import io.vertx.core.AbstractVerticle;

import java.util.UUID;

public class HelloVerticle extends AbstractVerticle {

  // java -jar target/starter-1.0.0-SNAPSHOT.jar -cluster -Djava.net.preferIPv4Stack=true -Dhttp.port=8090
  String verticleId = UUID.randomUUID().toString();

  @Override
  public void start() {
    vertx.eventBus().consumer("hello.vertx.addr", msg -> {
      msg.reply("Hello from Vert.x!");
    });

    vertx.eventBus().consumer("hello.named.addr", msg -> {
      String name = msg.body().toString();
      msg.reply(String.format("Hello %s, from %s", name, verticleId));
    });
  }
}
