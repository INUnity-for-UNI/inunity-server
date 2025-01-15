package com.inu.inunity.common.fcm.notification;

public enum NotificationMessage {

    NEW_MESSAGE("게시물에 새로운 댓글이 달렸어요");


    String message;

    NotificationMessage(String message) {
        this.message = message;
    }
}
