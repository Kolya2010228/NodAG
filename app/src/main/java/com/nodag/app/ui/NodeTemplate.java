package com.nodag.app.ui;

import com.nodag.app.nodes.NodeType;

/**
 * Шаблон ноды для отображения в списке
 */
public class NodeTemplate {
    private String name;
    private NodeType type;
    private String icon;

    public NodeTemplate(String name, NodeType type, String icon) {
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public String getName() { return name; }
    public NodeType getType() { return type; }
    public String getIcon() { return icon; }
}
