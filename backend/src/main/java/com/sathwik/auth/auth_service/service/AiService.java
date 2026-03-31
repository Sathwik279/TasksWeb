package com.sathwik.auth.auth_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sathwik.auth.auth_service.repository.TodoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.model}")
    private String model;

    private final TodoRepository todoRepo;
    private final ObjectMapper objectMapper;

    public AiService(TodoRepository todoRepo) {
        this.todoRepo = todoRepo;
        this.objectMapper = new ObjectMapper();
    }

    // Generate AI content synchronously and persist. Returns the generated text.
    public String generateAndSaveAiContent(String todoId, String prompt, String userId) {
        try {
            // Step A: Call Gemini (The CPU is active because the PUT request is still open)
            String aiText = askLLM(prompt);

            // Step B: Update the Database
            return todoRepo.findById(todoId).map(todo -> {
                todo.setAiContent(aiText);
                todoRepo.save(todo);
                return aiText;
            }).orElse("Todo not found");

        } catch (Exception e) {
            System.err.println("AI Generation failed: " + e.getMessage());
            return "AI failed to generate content.";
        }
    }

    public String askLLM(String prompt) {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        try {
            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // Build request payload using Map for safe JSON construction
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("role", "user");
            messageMap.put("content", "Briefly provide a helpful summary or insight/tip for the following task: " + prompt);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(messageMap));
            requestBody.put("temperature", 0.7);

            // Serialize to JSON string
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            // Execute request
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(url, request, String.class);

            // Parse response JSON using Jackson
            if (response != null) {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode content = rootNode.path("choices").get(0).path("message").path("content");
                if (content.isTextual()) {
                    return content.asText();
                }
            }
            return "No AI response";
        } catch (Exception e) {
            System.err.println("❌ AI API CALL FAILED: " + e.getMessage());
            e.printStackTrace();
            return "AI Generation failed due to network. Please save again.";
        }
    }
}
