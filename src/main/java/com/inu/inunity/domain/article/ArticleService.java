package com.inu.inunity.domain.article;

import com.inu.inunity.domain.article.dto.RequestCreateArticle;
import com.inu.inunity.domain.article.dto.RequestModifyArticle;
import com.inu.inunity.domain.article.dto.ResponseArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    /**
     * 아티클을 생성하는 메서드
     * @author 김원정
     * @param requestCreateArticle RequestCreateArticle Record
     * @return Long 생성된 아티클의 게시글 번호
     */
    public Long createArticle(RequestCreateArticle requestCreateArticle) {
        Article newArticle = Article.builder()
                .title(requestCreateArticle.title())
                .content(requestCreateArticle.content())
                .isAnonymous(requestCreateArticle.isAnonymous())
                .view(0)
                .isDeleted(false)
                .build();

        Article savedArticle = articleRepository.save(newArticle);
        return savedArticle.getId();
    }

    /**
     * 아티클 단 건 조회 메서드
     * @author 김원정
     * @param article_id 아티클 ID
     * @return responseArticle Record
     */
    public ResponseArticle getArticle(Long article_id) {
        Article foundArticle = articleRepository.findById(article_id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        foundArticle.increaseView();
        Article savedArticle = articleRepository.save(foundArticle);
        return ResponseArticle.builder()
                .id(savedArticle.getId())
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .view(savedArticle.getView())
                .isDeleted(savedArticle.getIsDeleted())
                .isAnonymous(savedArticle.getIsAnonymous())
                .build();
    }

    /**
     * 아티클 업데이트 메서드
     * @author 김원정
     * @param article_id 수정될 아티클의 ID
     * @param requestModifyArticle 수정된 Record
     * @return Long 수정된 아티클의 게시글 번호
     */
    public Long modifyArticle(Long article_id, RequestModifyArticle requestModifyArticle) {
        Article foundArticle = articleRepository.findById(article_id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        foundArticle.modifyArticle(requestModifyArticle);
        Article savedArticle = articleRepository.save(foundArticle);
        return savedArticle.getId();
    }

    /**
     * 아티클 삭제 메서드
     * @author 김원정
     * @param article_id 삭제될 아티클의 ID
     */
    public void deleteArticle(Long article_id) {
        articleRepository.deleteById(article_id);
    }

}
