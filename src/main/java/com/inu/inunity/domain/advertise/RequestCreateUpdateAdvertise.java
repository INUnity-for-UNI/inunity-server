package com.inu.inunity.domain.advertise;

public record RequestCreateUpdateAdvertise(
        String title,
        String content,
        String url
) {
}
