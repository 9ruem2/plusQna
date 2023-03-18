package com.QnaApi.board;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.criterion.Order;
import org.springframework.validation.annotation.Validated;
import com.QnaApi.board.Board.ContentStatus;

@Setter
@Validated
public class BoardPatchDto {
    private Long memberId;
    private Long boardId;
    private String content;
    private String title;
    private Board.ContentStatus contentStatus; //Enum은 정의할때만 쓰는거라 타입만 쓰면 됨
}
