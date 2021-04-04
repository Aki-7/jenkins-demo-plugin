package io.jenkins.plugins.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class MetricsServer extends Thread {
  private MetricRegistry metrics;
  private int port;

  public MetricsServer(MetricRegistry metrics, int port) {
    this.metrics = metrics;
    this.port = port;
  }

  @Override
  public void run() {
    try {
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      server.createContext("/", new Handler(metrics));
      System.out.println("Server listening on port: " + port);
      server.start();
    } catch (IOException e) {}
  }

  private final static class Handler implements HttpHandler {
    private MetricRegistry metrics;

    public Handler(MetricRegistry metrics) {
      this.metrics = metrics;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
      Headers resHeaders = t.getResponseHeaders();
      resHeaders.set("Content-type", "application/json");
      String body = metricsJson();
      long contentLength = body.getBytes(StandardCharsets.UTF_8).length;
      t.sendResponseHeaders(200, contentLength);

      OutputStream os = t.getResponseBody();
      os.write(body.getBytes());
      os.close();
    }

    private String metricsJson() {
      String json = "";
      ObjectMapper jsonMapper = new ObjectMapper()
        .registerModule((new MetricsModule(TimeUnit.SECONDS, TimeUnit.MILLISECONDS, false)));
      try {
        json = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metrics);
      } catch (JsonProcessingException e) {
        json = "Fail to parse json";
      }
      return json;
    }

  }
}
