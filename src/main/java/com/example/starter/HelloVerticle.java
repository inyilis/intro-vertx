package com.example.starter;

import io.vertx.core.AbstractVerticle;

public class HelloVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.eventBus().consumer("hello.vertx.addr", msg -> {
      msg.reply("Hello from Vert.x!");
    });

    vertx.eventBus().consumer("hello.named.addr", msg -> {
      String name = msg.body().toString();
      msg.reply(String.format("Hello %s!", name));
    });
  }
}
