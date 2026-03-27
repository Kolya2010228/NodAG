package com.nodag.app.nodes;

/**
 * Порт ноды
 */
public class NodePort {
    private final String name;
    private final PortType type;
    private final PortDirection direction;
    
    private boolean isConnected;
    private String connectedTo;

    public NodePort(String name, PortType type, PortDirection direction) {
        this.name = name;
        this.type = type;
        this.direction = direction;
        this.isConnected = false;
        this.connectedTo = null;
    }

    public String getName() { return name; }
    public PortType getType() { return type; }
    public PortDirection getDirection() { return direction; }
    public boolean isConnected() { return isConnected; }
    public void setConnected(boolean connected) { isConnected = connected; }
    public String getConnectedTo() { return connectedTo; }
    public void setConnectedTo(String connectedTo) { this.connectedTo = connectedTo; }
}
