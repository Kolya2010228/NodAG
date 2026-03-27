package com.nodag.app.workflow;

import com.nodag.app.nodes.NodeResult;

import java.util.List;
import java.util.Map;

/**
 * Результат выполнения воркфлоу
 */
public class WorkflowResult {
    private final boolean success;
    private final Map<String, NodeResult> nodeResults;
    private final List<String> failedNodes;
    private final String error;

    public WorkflowResult(boolean success, Map<String, NodeResult> nodeResults, 
                          List<String> failedNodes, String error) {
        this.success = success;
        this.nodeResults = nodeResults;
        this.failedNodes = failedNodes;
        this.error = error;
    }

    public boolean isSuccess() { return success; }
    public Map<String, NodeResult> getNodeResults() { return nodeResults; }
    public List<String> getFailedNodes() { return failedNodes; }
    public String getError() { return error; }
}
