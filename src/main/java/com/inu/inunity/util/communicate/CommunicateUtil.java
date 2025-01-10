package com.inu.inunity.util.communicate;

import com.inu.inunity.util.communicate.dto.ResponseCleanBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommunicateUtil {
    private final RestClient restClientForAI;

    public String requestToCleanBot(String text) {
        ResponseCleanBot result = restClientForAI.get()
                .uri(uriBuilder -> uriBuilder.path("/cleanBot")
                        .queryParam("text", text)
                        .build())
                .retrieve()
                .body(ResponseCleanBot.class);
        return result.getFiltered_text();
    }
}
