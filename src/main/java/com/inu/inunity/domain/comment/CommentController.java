package com.inu.inunity.domain.comment;

import com.inu.inunity.common.CommonResponse;
import com.inu.inunity.domain.comment.dto.RequestCreateComment;
import com.inu.inunity.domain.comment.dto.RequestUpdateComment;
import com.inu.inunity.domain.replyComment.ReplyCommentService;
import com.inu.inunity.domain.replyComment.dto.RequestCreateReplyComment;
import com.inu.inunity.domain.replyComment.dto.RequestUpdateReplyComment;
import com.inu.inunity.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ReplyCommentService replyCommentService;

    @PostMapping("/v1/articles/{articleid}/comment")
    ResponseEntity<CommonResponse<Long>> createComment(
            @RequestBody RequestCreateComment requestCreateComment,
            @PathVariable Long articleid,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((CustomUserDetails) userDetails).getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success("댓글 생성 완료", commentService.createComment(requestCreateComment, articleid, userId)));
    }

    @PutMapping("/v1/articles/{articleid}/comment")
    CommonResponse<Long> updateComment(
            @RequestBody RequestUpdateComment requestUpdateComment,
            @PathVariable Long articleid,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((CustomUserDetails) userDetails).getId();
        return CommonResponse.success("댓글 수정 완료", commentService.modifyComment(requestUpdateComment));
    }

    @DeleteMapping("/v1/comment/{commentid}")
    CommonResponse<?> deleteComment(
            @RequestBody RequestUpdateComment requestUpdateComment,
            @PathVariable Long commentid,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((CustomUserDetails) userDetails).getId();
        commentService.deleteComment(commentid);
        return CommonResponse.success("댓글 삭제 완료", null);
    }

    @PostMapping("/v1/replycomment")
    public ResponseEntity<CommonResponse<Long>> createReplyComment(
            @RequestBody RequestCreateReplyComment requestCreateReplyComment,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((CustomUserDetails) userDetails).getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success("대댓글 생성 완료", replyCommentService.createReplyComment(requestCreateReplyComment, userId)));
    }

    @PutMapping("/v1/replycomment")
    public CommonResponse<Long> updateReplyComment(
            @RequestBody RequestUpdateReplyComment requestUpdateReplyComment,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((CustomUserDetails) userDetails).getId();
        return CommonResponse.success("대댓글 수정 완료", replyCommentService.modifyReplyComment(requestUpdateReplyComment));
    }

    @DeleteMapping("/v1/replycomment/{replycommentid}")
    public CommonResponse<?> deleteReplyComment(
            @PathVariable Long replycommentid,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((CustomUserDetails) userDetails).getId();
        replyCommentService.deleteReplyComment(replycommentid);
        return CommonResponse.success("대댓글 삭제 완료", null);
    }
}
