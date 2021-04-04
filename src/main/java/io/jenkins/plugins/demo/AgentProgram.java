package io.jenkins.plugins.demo;

import com.codahale.metrics.MetricRegistry;

import jenkins.security.MasterToSlaveCallable;

public final class AgentProgram extends MasterToSlaveCallable<Void, RuntimeException> {
  private static int PORT = 9000;
  private static final long serialVersionUID = 1L;

  @Override
  public Void call() {
    new AgentProgramThread().start();
    return null;
  }

  private final static class AgentProgramThread extends Thread {
    private MetricRegistry metrics;
    private MetricsServer server;

    public AgentProgramThread() {
      metrics = new MetricRegistry();
      new ConnectionStatusMetrics(metrics, "remote.connection.isConnected");
      server = new MetricsServer(metrics, PORT);
    }

    @Override
    public void run() {
      server.start();
      try {
        server.join();
      } catch (InterruptedException e) {}
    }
  }
}
