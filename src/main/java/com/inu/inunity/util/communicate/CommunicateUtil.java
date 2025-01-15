package com.inu.inunity.util.communicate;

import com.inu.inunity.util.communicate.dto.ResponseCleanBot;
import com.inu.inunity.util.communicate.dto.ResponseSummaryNotice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommunicateUtil {
    private final RestClient restClientForAI;

    public Boolean requestToCleanBot(String text) {
        Map<String, String> request = new HashMap<>();
        request.put("text", text);
        ResponseCleanBot result = restClientForAI.post()
                .uri("/cleanBot")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .retrieve()
                .body(ResponseCleanBot.class);
        return Objects.requireNonNull(result).isBinary_class();
    }

    public String requestToSummaryBot(String text) {
        Map<String, String> requestBody = Map.of("text", text);
        ResponseSummaryNotice result = restClientForAI.post()
                .uri("/summaryBot")
                .body(requestBody)
                .retrieve()
                .body(ResponseSummaryNotice.class);
        return result.getSummary();
    }


}
