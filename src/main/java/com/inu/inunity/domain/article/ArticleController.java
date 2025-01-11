package com.inu.inunity.domain.article;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.article.dto.RequestCreateArticle;
import com.inu.inunity.domain.article.dto.RequestModifyArticle;
import com.inu.inunity.domain.article.dto.ResponseArticle;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    /**
     * 아티클 생성 메서드
     * @author 김원정
     * @param requestCreateArticle @RequestBody Record Class
     * @param category_id @Path
     * @return CommonResponse에 감싸진 Long Category ID
     */
    @PostMapping("/v1/articles/{category_id}")
    CommonResponse<Long> createArticle(
            @RequestBody RequestCreateArticle requestCreateArticle,
            @PathVariable Long category_id,
            @AuthenticationPrincipal UserDetails userDetails
            ) throws JsonProcessingException {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long userId = customUserDetails.getId();
        Long result = articleService.createArticle(requestCreateArticle, category_id, userId);
        return CommonResponse.success("아티클 생성 완료", result);
    }

    @PostMapping("/v1/articles/{article_id}/like")
    CommonResponse<Integer> pushArticleLike(
            @PathVariable Long article_id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return CommonResponse.success("좋아요 누르기 성공", articleService.pushArticleLike(article_id, userDetails));
    }

    /**
     * 아티클 단 건 조회 메서드
     * @author 김원정
     * @param article_id @PathVariable Long 아티클 ID
     * @return CommonResponse에 감싸진 ResponseArticle 클래스
     */
    @GetMapping("/v1/articles/{article_id}")
    CommonResponse<ResponseArticle> getArticle(@PathVariable Long article_id,
                                               @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        ResponseArticle result = articleService.getArticle(article_id, userDetails);
        return CommonResponse.success("아티클 조회 완료", result);
    }

    /**
     * 아티클 업데이트 메서드
     * @author 김원정
     * @param article_id @PathVariable 수정될 아티클의 ID
     * @param requestModifyArticle @RequestBody RequestModifyArticle 수정된 Record Class
     * @return CommonResponse에 감싸진 Long Category ID
     */
    @PutMapping("/v1/articles/{article_id}")
    CommonResponse<Long> updateArticle(
            @PathVariable Long article_id,
            @RequestBody RequestModifyArticle requestModifyArticle,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long result = articleService.modifyArticle(article_id, requestModifyArticle, userDetails);
        return CommonResponse.success("아티클 수정 완료", result);
    }

    /**
     * 아티클 삭제 메서드
     * @author 김원정
     * @param article_id @PathVariable 삭제될 아티클의 ID
     */
    @DeleteMapping("/v1/articles/{article_id}")
    CommonResponse<Void> deleteArticle(@PathVariable Long article_id) {
        articleService.deleteArticle(article_id);
        return CommonResponse.success("아이클 삭재 완료", null);
    }
}
