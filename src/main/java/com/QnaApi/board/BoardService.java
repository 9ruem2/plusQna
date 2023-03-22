package com.QnaApi.board;

import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


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
    public void cancelOrder(Long boardId) {
        Board findBoard = boardManager.findVerifiedBoard(boardId);
        findBoard.setQuestionStatus(Board.QuestionStatus.QUESTION_DELETE);
    }
}
