package com.inu.inunity.common.editorJS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inu.inunity.common.exception.EditorJSConvertException;
import com.inu.inunity.common.exception.ExceptionMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EditorJSConverter {

    private final ObjectMapper objectMapper;

    public EditorJSConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public EditorJSOutput convertHtmlToEditorJS(String html) {
        EditorJSOutput output = new EditorJSOutput();

        // Convert HTML to Block
        Block block = new Block();
        BlockData blockData = new BlockData();
        blockData.setHtml(html);
        block.setData(blockData);

        output.getBlocks().add(block);

        return output;
    }

    public String extractTextFromEditorJS(EditorJSOutput output) {
        StringBuilder textContent = new StringBuilder();

        for (Block block : output.getBlocks()) {
            String type = block.getType();
            BlockData data = block.getData();

            if (data != null) {
                switch (type) {
                    case "paragraph":
                    case "header":
                    case "quote":
                        if (data.getText() != null) {
                            textContent.append(data.getText()).append(" ");
                        }
                        break;

                    case "list":
                        if (data.getItems() != null) {
                            extractListItems(data.getItems(), textContent);
                        }
                        break;

                    default:
                        // Ignore unsupported types
                        break;
                }
            }
        }

        return textContent.toString().trim();
    }

    private void extractListItems(List<ListItem> items, StringBuilder textContent) {
        if (items != null) {
            for (ListItem item : items) {
                if (item.getContent() != null) {
                    textContent.append(item.getContent()).append(" ");
                }
                // Recursive call for nested items
                if (item.getItems() != null) {
                    extractListItems(item.getItems(), textContent);
                }
            }
        }
    }

    public String toJson(EditorJSOutput output) {
        try {
            return objectMapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new EditorJSConvertException(ExceptionMessage.EDITOR_JS_CONVERT_FAILED);
        }
    }
}