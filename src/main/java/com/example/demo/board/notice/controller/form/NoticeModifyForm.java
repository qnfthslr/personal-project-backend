package com.example.demo.board.notice.controller.form;

import com.example.demo.board.notice.entity.NoticeBoard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeModifyForm {
    final private String title;
    final private String content;

    public NoticeBoard toNoticeBoard() {
        return new NoticeBoard(title,content);
    }
}
