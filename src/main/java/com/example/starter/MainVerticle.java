package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.deployVerticle(new HelloVerticle());
    Router router = Router.router(vertx);

    router.get("/api/v1/hello").handler(this::helloVertx);
    router.get("/api/v1/hello/:name").handler(this::helloName);

    vertx.createHttpServer().requestHandler(router).listen(8888);
  }

  void helloVertx(RoutingContext routingContext) {
    vertx.eventBus().request("hello.vertx.addr", "", reply -> {
      routingContext.request().response().end(reply.result().body().toString());
    });
  }

  void helloName(RoutingContext routingContext) {
    String name = routingContext.pathParam("name");
    vertx.eventBus().request("hello.named.addr", name, reply -> {
      routingContext.request().response().end(reply.result().body().toString());
    });
  }
}
