vertx.eventBus().consumer("hello.vertx.addr", function(msg) {
  msg.reply("Hello from Vert.x!, from js");
});
