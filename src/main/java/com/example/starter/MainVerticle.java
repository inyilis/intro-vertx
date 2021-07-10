package com.example.starter;

import com.example.starter.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(MainVerticle.class);
  }

  @Override
  public void start() {
    vertx.deployVerticle(new HelloVerticle());
    Router router = Router.router(vertx);

    router.get("/api/v1/hello").handler(this::helloVertx);
    router.get("/api/v1/hello/:name").handler(this::helloName);

    int httpPort;
    try {
      httpPort = Integer.parseInt(System.getProperty("http.port", "8888"));
    } catch (NumberFormatException nfe) {
      httpPort = 8888;
    }

    vertx.createHttpServer().requestHandler(router).listen(httpPort);
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
