package com.inu.inunity.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionMessage {
    TOKEN_EXPIRED("토큰이 만료되었습니다.", 0, HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_AUTHORIZED("권한이 없습니다", 0, HttpStatus.FORBIDDEN),
    TOKEN_UNAUTHENTICATED("인증되지 않은 토큰입니다.", 0, HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID_FORMAT("잘못된 형식의 토큰입니다.", 0, HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("유저가 존재하지 않습니다.", 0, HttpStatus.NOT_FOUND),
    TOKEN_NOT_FOUND("토큰이 비었거나 null입니다", 0, HttpStatus.BAD_REQUEST),
    TOKEN_TYPE_INVALID("토큰 타입이 틀렸습니다.", 0, HttpStatus.BAD_REQUEST),
    FAILED_VALIDATION("유효성 검사 실패", 0, HttpStatus.BAD_REQUEST),
    EMAIL_NOT_INU("인천대학교 이메일이 아닌데요?!", 0, HttpStatus.BAD_REQUEST),
    EMAIL_NOT_UNDEFINED("이메일의 이름이 참... 이걸론 인증 몬합니다!", 0, HttpStatus.BAD_REQUEST),
    USER_NOT_INFORMATION_MAJOR("정보대 구성원이 아닌데요!", 0, HttpStatus.BAD_REQUEST),
    USER_ALREADY_REGISTERED("이미 가입된 유저입니다", 0, HttpStatus.BAD_REQUEST),
    USER_NOT_REGISTERED("서비스에 가입되지 않은 유저입니다.", 0, HttpStatus.BAD_REQUEST),
    PORTAL_LOGIN_FAILED_ID_PASSWORD_INCORRECT("포탈 로그인 실패: 아이디비번 틀림", 0, HttpStatus.BAD_REQUEST),
    PORTAL_LOGIN_FAILED_SERVER_ERROR("포탈 로그인 실패: 예기치않은 서버 통신 오류", 0, HttpStatus.BAD_REQUEST),
    CONTRACT_NOT_FOUND("SNS/Link 존재하지 않습니다.", 0, HttpStatus.NOT_FOUND),
    CAREER_NOT_FOUND("경력 이력이 존재하지 않습니다.", 0, HttpStatus.NOT_FOUND),
    SKILL_NOT_FOUND("스킬이 존재하지 않습니다.", 0, HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다.", 0, HttpStatus.NOT_FOUND),
    ARTICLE_NOT_FOUND("아티클을 찾을 수 없습니다.", 0, HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", 0, HttpStatus.NOT_FOUND),
    ARTICLE_LIKE_NOT_FOUND("유저가 좋아요를 누른 기록을 찾을 수 없습니다.", 0, HttpStatus.NOT_FOUND),
    ARTICLE_IS_DELETED("삭제된 게시글입니다.", 0, HttpStatus.NOT_FOUND)
    ;


    private final String message;

    private final Integer errorCode;

    private final HttpStatus httpStatus;


    ExceptionMessage(String message, Integer errorCode, HttpStatus httpStatus) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
