package com.example.starter;

import com.example.starter.util.Runner;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(MainVerticle.class);
  }

  @Override
  public void start(Promise<Void> start) {
//    vertx.deployVerticle(new HelloVerticle());
    vertx.deployVerticle("Hello.groovy");
    vertx.deployVerticle("Hello.js");

    Router router = Router.router(vertx);

    router.get("/api/v1/hello").handler(this::helloVertx);
    router.get("/api/v1/hello/:name").handler(this::helloName);
    router.route().handler(StaticHandler.create("web").setIndexPage("index.html"));

    doConfig(start, router);
  }

  void doConfig(Promise<Void> start, Router router) {
    ConfigStoreOptions defaultConfig = new ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(new JsonObject().put("path", "config.json"));

    ConfigRetrieverOptions opts = new ConfigRetrieverOptions()
      .addStore(defaultConfig);

    ConfigRetriever configRetriever = ConfigRetriever.create(vertx, opts);

    Handler<AsyncResult<JsonObject>> handler = asyncResult -> this.handleConfigResult(start, router, asyncResult);
    configRetriever.getConfig(handler);
  }

  void handleConfigResult(Promise<Void> start, Router router, AsyncResult<JsonObject> asyncResult) {
    if (asyncResult.succeeded()) {
      JsonObject config = asyncResult.result();
      JsonObject http = config.getJsonObject("http");
      int httpPort = http.getInteger("port");
      vertx.createHttpServer().requestHandler(router).listen(httpPort);
      start.complete();
    } else {
      start.fail("Unable to load configuration");
    }
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
