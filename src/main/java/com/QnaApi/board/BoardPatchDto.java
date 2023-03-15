package com.QnaApi.board;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.criterion.Order;
import org.springframework.validation.annotation.Validated;


@Setter
@Validated
public class BoardPatchDto {
    private Long memberId;
    private Long boardId;
    private String content;
    private String title;
    private enum Board.content_status;
}
