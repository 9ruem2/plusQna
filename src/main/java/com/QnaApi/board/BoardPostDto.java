package com.QnaApi.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

@Getter
@Validated
public class BoardPostDto {
    @Positive
    private Long memberId;
    @NotBlank
    private String content;
    @NotBlank
    private String title;

    @NotNull
    private Board.ContentStatus contentStatus; //공개 비공개
}
