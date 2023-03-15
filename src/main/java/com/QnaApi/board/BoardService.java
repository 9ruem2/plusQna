package com.QnaApi.board;

import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final Board board;

    // 게시글추가
    public Board createBoard(Board board) {
       return boardRepository.save(board);
    }

    // 게시글 업데이트
    public Board updateBoard(Board board){
        Board findBoard = findVerifiedBoard(board.getBoardId());

        Optional.ofNullable(board.getQuestion_status())
                .ifPresent(boardStatus -> findBoard.setQuestion_status(boardStatus);
        return saveBoard(findBoard);
    }

    //게시글이 존재하는지 확인
    public Board findVerifiedBoard(Long boardId){
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board findBoard =
                optionalBoard.orElseThrow(()->
                    new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        return findBoard;
    }

    // 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
    public void cancelOrder(Long boardId) {
        Board findBoard = findVerifiedBoard(boardId);
        findBoard.setQuestionStatus(Board.Question_status.QUESTION_DELETE);
        //boardRepository.delete(deleteBoard);
    }
}
