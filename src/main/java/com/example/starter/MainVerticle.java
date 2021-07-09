package com.example.starter;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      req.response().end("Hello from Vert.x!");
    }).listen(8888);
  }
}
