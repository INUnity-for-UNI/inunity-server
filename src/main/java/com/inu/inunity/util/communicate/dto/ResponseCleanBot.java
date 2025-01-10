package com.inu.inunity.util.communicate.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ResponseCleanBot {
    private boolean binary_class;
    private double clean_score;
    private String filtered_text;
    private String original_text;
    private List<LabelScore> total;

    @Getter
    @ToString
    public static class LabelScore {
        private String label;
        private double score;
    }
}
