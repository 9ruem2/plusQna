package com.QnaApi.board.service;

import com.QnaApi.board.entity.Board;
import com.QnaApi.board.repository.BoardRepository;
import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final BoardManager boardManager;

    // 게시글추가
    // 회원가입이 된 멤버만 글을 쓸 수 있다는 조건
    public Board createBoard(Board board) {
        // 회원가입이 된 멤버인지 확인하기
        memberService.findVerifiedMember(board.getMember().getMemberId());

        return saveBoard(board);
    }

    // 게시글수정하기 로직
    // 세부동작들은 클래스를 하나 만들어주고 하는게 좋음 (이유는? 코드의 가독성과 재사용을 위해서)
    public Board updateBoard(Board board){ // 보드에서 업데이트하라는 요청이 왔을 때 받아주는 역할을 하는 로직으로 쓰임
        Board updatedBoard = boardManager.boardUpdater(board);
        // 게시글 저장하기
        return saveBoard(updatedBoard);
    }

    // 수정된 게시글을 다시 repository에 .save()저장하기
    private Board saveBoard(Board board){
        return boardRepository.save(board);
    }

    public Board findVerifiedBoard(Long boardId){
        Board board = boardManager.findVerifiedBoard(boardId);
        return board;
    }


// 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
/*
- 1건의 질문은 회원(고객)만 삭제할 수 있다.
- 1건의 질문 삭제는 질문을 등록한 회원만 가능하다.
- 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
- 이미 삭제 상태인 질문은 삭제할 수 없다.
*/
    public void cancelOrder(Board board) {
        Board findBoard = findVerifiedBoard(board.getBoardId());
        Board deletedBoard = boardManager.delteContent(findBoard);
       // 변경된 Status를 REPOSITORY에 저장
        boardRepository.save(deletedBoard);
    }

    public void isDeleted(Board board){
        //- 보드의 상태가 delete인지 확인 하고 delete라면 삭제상태라면 보드를 읽어올 수 없다고 예외를 던짐
        if(board.getQuestionStatus()==Board.QuestionStatus.QUESTION_DELETE){
            throw new BusinessLogicException(ExceptionCode.BOARD_HAS_BEEN_DELETED);
        }
    }

//    게시글이 공개인지, 비공개인지 확인하기
    public boolean getPublicationStatus(Board board){
        boolean boardSecret = false;
        if(board.getContentStatus().equals(Board.ContentStatus.SECRET)){
            return boardSecret = true; // 게시글 = 비공개 상태
        }
        return boardSecret;
    }

    //1건의 게시글을 조회하는 메서드
    public void getArticle(Board board){
        boolean boardSecret = getPublicationStatus(board);
        if(boardSecret) {
            // 게시글이 비공개 상태라면
            // 게시글을 조회하려는 사람의 memberId와 저장되어있는 게시글을 작성한 memberId가 같은지 확인하기
            boardManager.checkNotExistBoard(board);
        }
    }

    //  1건의 질문 조회 시 질문에 대한 답변이 존재한다면 답변도 함께 조회되어야 한다.


}
