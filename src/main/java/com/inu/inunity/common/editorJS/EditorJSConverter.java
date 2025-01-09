package com.inu.inunity.common.editorJS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inu.inunity.common.exception.EditorJSConvertException;
import com.inu.inunity.common.exception.ExceptionMessage;
import org.springframework.stereotype.Component;

@Component
public class EditorJSConverter {

    private final ObjectMapper objectMapper;

    public EditorJSConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public EditorJSOutput convertHtmlToEditorJS(String html) {
        EditorJSOutput output = new EditorJSOutput();

        Block block = new Block();
        BlockData blockData = new BlockData();
        blockData.setHtml(html);
        block.setData(blockData);

        output.getBlocks().add(block);

        return output;
    }

    public String toJson(EditorJSOutput output) {
        try {
            return objectMapper.writeValueAsString(output);
        } catch (JsonProcessingException e) {
            throw new EditorJSConvertException(ExceptionMessage.EDITOR_JS_CONVERT_FAILED);
        }
    }
}
