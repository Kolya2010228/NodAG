package com.nodag.app.ui;

import com.nodag.app.nodes.PortDirection;
import com.nodag.app.nodes.PortType;

/**
 * Элемент порта
 */
public class PortItem {
    private String name;
    private PortType type;
    private PortDirection direction;

    public PortItem(String name, PortType type, PortDirection direction) {
        this.name = name;
        this.type = type;
        this.direction = direction;
    }

    public String getName() { return name; }
    public PortType getType() { return type; }
    public PortDirection getDirection() { return direction; }
}
