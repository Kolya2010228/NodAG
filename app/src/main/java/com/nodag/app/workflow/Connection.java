package com.nodag.app.workflow;

/**
 * Соединение между нодами
 */
public class Connection {
    private String id;
    private String fromNodeId;
    private String fromPort;
    private String toNodeId;
    private String toPort;

    public Connection(String id, String fromNodeId, String fromPort, 
                      String toNodeId, String toPort) {
        this.id = id;
        this.fromNodeId = fromNodeId;
        this.fromPort = fromPort;
        this.toNodeId = toNodeId;
        this.toPort = toPort;
    }

    public String getId() { return id; }
    public String getFromNodeId() { return fromNodeId; }
    public String getFromPort() { return fromPort; }
    public String getToNodeId() { return toNodeId; }
    public String getToPort() { return toPort; }
}
