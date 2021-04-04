package io.jenkins.plugins.demo;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import hudson.remoting.Engine;
import hudson.remoting.EngineListener;

public final class ConnectionStatusMetrics implements EngineListener {
  private boolean isConnected = true;

  public ConnectionStatusMetrics(MetricRegistry metrics, String name) {
    Engine.current().addListener(this);
    metrics.register(name, new Gauge<Boolean>(){
      @Override
      public Boolean getValue() {
        return getConnectionStatus();
      }
    });
  }

  @Override
  public void status(String msg) {}

  @Override
  public void status(String msg, Throwable t) {}

  @Override
  public void error(Throwable t) {}

  @Override
  public void onDisconnect() {
    isConnected = false;
  }

  @Override
  public void onReconnect() {
    // TODO Auto-generated method stub
    isConnected = true;
  }

  public boolean getConnectionStatus() {
    return isConnected;
  }
}
