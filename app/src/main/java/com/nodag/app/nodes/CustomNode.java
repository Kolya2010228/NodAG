package com.nodag.app.nodes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Кастомная нода, создаваемая пользователем
 * Позволяет подключаться к любому ИИ API
 */
public class CustomNode extends BaseNode {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    private String customType;
    private String httpMethod;
    private String requestBodyTemplate;
    private Map<String, String> responseMapping;
    private Map<String, String> headers;

    public CustomNode(String id, String name, String customType) {
        super(id, name, NodeType.CUSTOM);
        this.customType = customType;
        this.httpMethod = "POST";
        this.requestBodyTemplate = "";
        this.responseMapping = new HashMap<>();
        this.headers = new HashMap<>();
        
        // По умолчанию добавляем стандартные порты
        addInput("Промпт", PortType.TEXT);
        addInput("API Key", PortType.TEXT);
        addOutput("Ответ", PortType.TEXT);
    }

    public CustomNode(String id, String name) {
        this(id, name, "API_CALL");
    }

    @Override
    public NodeResult execute(Map<String, Object> inputData) {
        try {
            String prompt = inputData.containsKey("Промпт") 
                ? (String) inputData.get("Промпт") 
                : "";
            String apiKey = inputData.containsKey("API Key") 
                ? (String) inputData.get("API Key") 
                : this.apiKey;

            if (apiEndpoint == null || apiEndpoint.isBlank()) {
                return NodeResult.failure("API Endpoint не указан");
            }

            if (apiKey == null || apiKey.isBlank()) {
                return NodeResult.failure("API Key не указан");
            }

            // Формируем тело запроса
            String requestBody = requestBodyTemplate != null && !requestBodyTemplate.isBlank()
                ? requestBodyTemplate.replace("{prompt}", prompt != null ? prompt : "")
                : "{\"prompt\": \"" + (prompt != null ? prompt : "") + "\"}";

            // Отправляем запрос
            String response = sendHttpRequest(apiEndpoint, apiKey, requestBody);

            // Парсим ответ согласно маппингу
            Map<String, Object> resultData = parseResponse(response);

            return NodeResult.success(resultData);
        } catch (Exception e) {
            return NodeResult.failure("Ошибка выполнения: " + e.getMessage());
        }
    }

    private String sendHttpRequest(String urlString, String apiKey, String body) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod(httpMethod);
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            // Заголовки
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            if (headers != null) {
                headers.forEach(connection::setRequestProperty);
            }

            // Тело запроса
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
            }

            // Читаем ответ
            int responseCode = connection.getResponseCode();
            BufferedReader reader = responseCode >= 400
                ? new BufferedReader(new InputStreamReader(connection.getErrorStream()))
                : new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    private Map<String, Object> parseResponse(String response) {
        Map<String, Object> result = new HashMap<>();

        if (responseMapping == null || responseMapping.isEmpty()) {
            // Если маппинг не настроен, возвращаем весь ответ
            result.put("Ответ", response);
        } else {
            // Парсим согласно маппингу (упрощенно)
            responseMapping.forEach((outputPort, jsonPath) -> {
                String value = extractJsonValue(response, jsonPath);
                result.put(outputPort, value);
            });
        }

        return result;
    }

    private String extractJsonValue(String json, String path) {
        // Упрощенное извлечение значения по пути
        // В реальной реализации нужно использовать JSON парсер
        return json;
    }

    @Override
    public boolean validate() {
        return super.validate() && apiEndpoint != null && !apiEndpoint.isBlank();
    }

    @Override
    public BaseNode clone() {
        CustomNode clone = new CustomNode(id, name, customType);
        clone.inputs.addAll(inputs);
        clone.outputs.addAll(outputs);
        clone.apiKey = apiKey;
        clone.apiEndpoint = apiEndpoint;
        clone.httpMethod = httpMethod;
        clone.requestBodyTemplate = requestBodyTemplate;
        clone.responseMapping = new HashMap<>(responseMapping);
        clone.headers = new HashMap<>(headers);
        clone.positionX = positionX;
        clone.positionY = positionY;
        return clone;
    }

    // Геттеры и сеттеры
    public String getCustomType() { return customType; }
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    public String getRequestBodyTemplate() { return requestBodyTemplate; }
    public void setRequestBodyTemplate(String requestBodyTemplate) { this.requestBodyTemplate = requestBodyTemplate; }
    public Map<String, String> getResponseMapping() { return responseMapping; }
    public void setResponseMapping(Map<String, String> responseMapping) { this.responseMapping = responseMapping; }
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    /**
     * Конфигурация кастомной ноды
     */
    public static class Config {
        public String name;
        public String apiEndpoint;
        public String apiKey;
        public String httpMethod;
        public String requestBodyTemplate;
        public List<PortConfig> inputs;
        public List<PortConfig> outputs;
        public Map<String, String> responseMapping;

        public Config() {
            httpMethod = "POST";
            requestBodyTemplate = "";
        }
    }

    public static class PortConfig {
        public String name;
        public PortType type;
        public PortDirection direction;
    }
}
