package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);

    router.get("/api/v1/hello").handler(this::helloVertx);
    router.get("/api/v1/hello/:name").handler(this::helloName);

    vertx.createHttpServer().requestHandler(router).listen(8888);
  }

  void helloVertx(RoutingContext routingContext) {
    routingContext.request().response().end("Hello from Vert.x!");
  }

  void helloName(RoutingContext routingContext) {
    String name = routingContext.pathParam("name");
    routingContext.request().response().end(String.format("Hello %s!", name));
  }
}
