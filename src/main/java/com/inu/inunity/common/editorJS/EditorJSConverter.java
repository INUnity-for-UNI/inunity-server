package com.inu.inunity.common.editorJS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class EditorJSConverter {

    private final ObjectMapper objectMapper;

    public EditorJSConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractTextFromEditorJS(String content) {
        try {
            // Step 1: Parse the escaped JSON string in "content"
            JsonNode root = objectMapper.readTree(content);

            // Step 2: Extract "blocks" array
            JsonNode blocks = root.get("blocks");
            if (blocks == null || !blocks.isArray()) {
                return "";
            }

            StringBuilder textContent = new StringBuilder();

            // Step 3: Process each block
            for (JsonNode block : blocks) {
                String type = block.get("type").asText();
                JsonNode data = block.get("data");

                switch (type) {
                    case "paragraph":
                    case "header":
                    case "quote":
                        if (data != null && data.has("text")) {
                            textContent.append(data.get("text").asText()).append(" ");
                        }
                        break;

                    case "list":
                        if (data != null && data.has("items")) {
                            extractListItems(data.get("items"), textContent);
                        }
                        break;

                    case "raw":
                        if (data != null && data.has("html")) {
                            textContent.append(data.get("html").asText()).append(" ");
                        }
                        break;

                    default:
                        // Ignore unsupported types
                        break;
                }
            }

            return textContent.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from EditorJS content", e);
        }
    }

    private void extractListItems(JsonNode items, StringBuilder textContent) {
        if (items.isArray()) {
            for (JsonNode item : items) {
                if (item.has("content")) {
                    textContent.append(item.get("content").asText()).append(" ");
                }
                if (item.has("items")) {
                    extractListItems(item.get("items"), textContent);
                }
            }
        }
    }
}