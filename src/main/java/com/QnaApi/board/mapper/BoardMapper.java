package com.QnaApi.board.mapper;

import com.QnaApi.board.dto.*;
import com.QnaApi.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mapping(source = "memberId" ,target = "member.memberId") //소스:포스트DTO -> board// memberId->
    Board boardPostDtoToBoard(BoardPostDto boardPostDto);
    @Mapping(source = "memberId", target = "member.memberId")
    Board boardPatchDtoToBoard(BoardPatchDto boardPatchDto);

    @Mapping(source = "memberId", target = "member.memberId")
    Board boardDeleteDtoToBoard(BoardDeleteDto boardDeleteDto);

    @Mapping(source="memberId", target="member.memberId")
    Board boardGetDtoToBoard(BoardGetDto boardGetDto);

    BoardResponseDto BoardToBoardResponseDto(Board response);

    MultiResponseDto BoardToMultiResponsDto(BoardGetDto boardGetDto);
}
