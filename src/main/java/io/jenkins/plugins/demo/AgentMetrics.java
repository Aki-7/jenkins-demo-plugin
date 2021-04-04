package io.jenkins.plugins.demo;

import java.io.IOException;

import hudson.Extension;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.slaves.ComputerListener;
import jenkins.model.Jenkins.MasterComputer;

@Extension
public final class AgentMetrics extends ComputerListener {
  @Override
  public void onOnline(Computer c, TaskListener listener) throws IOException, InterruptedException {
    if (c instanceof MasterComputer) return;
    c.getChannel().call(new AgentProgram());
  }
}
