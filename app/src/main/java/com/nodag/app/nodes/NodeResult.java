package com.nodag.app.nodes;

import java.util.Map;

/**
 * Результат выполнения ноды
 */
public class NodeResult {
    private final boolean success;
    private final Map<String, Object> data;
    private final String error;

    public NodeResult(boolean success, Map<String, Object> data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static NodeResult success(Map<String, Object> data) {
        return new NodeResult(true, data, null);
    }

    public static NodeResult failure(String error) {
        return new NodeResult(false, null, error);
    }

    public boolean isSuccess() { return success; }
    public Map<String, Object> getData() { return data; }
    public String getError() { return error; }
}
