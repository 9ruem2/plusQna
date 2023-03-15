package com.QnaApi.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mapping(source = "memberId" ,target = "member.memberId") //소스:포스트DTO -> board// memberId->
    Board boardPostDtoToBoard(BoardPostDto boardPostDto);

    BoardResponseDto BoardToBoardResponseDto(Board response);

}
