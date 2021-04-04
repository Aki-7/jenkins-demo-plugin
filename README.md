# Jenkins Demo Plugin for GSoC 2021

## What is this?

This is a demo plugin for Jenkins. This plugin launches an HTTP server on every connected agent and exposes the agent and controller's connection status in JSON format.

#### **DO NOT** use in production.

## Launch

### Prepare for Plugin Development

see https://www.jenkins.io/doc/developer/tutorial/prepare/

### Start up

1. clone this repository.

2. execute the following command at the project root.

```
$ mvn hpi:run
```

3. access http://localhost:8080/jenkins

### Connect agents

See Jenkins documents.

### Access to the agent's metrics

access http://<agent.host>:9000/

```json
// response
{
  "version": "4.0.0",
  "gauges": {
    "remote.connection.isConnected": {
      "value": true
    }
  },
  "counters": {},
  "histograms": {},
  "meters": {},
  "timers": {}
}
```

You can confirm agent-controller connection is up.

### Disconnect the agent

1. Disconnect the agent from Web UI.

2. Access http://<agent.host>:9000/

```json
// response
{
  "version": "4.0.0",
  "gauges": {
    "remote.connection.isConnected": {
      "value": false
    }
  },
  "counters": {},
  "histograms": {},
  "meters": {},
  "timers": {}
}
```

You can confirm agent-controller connection is down.
