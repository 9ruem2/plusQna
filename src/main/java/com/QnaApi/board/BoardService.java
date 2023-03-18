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
    private final Board board;
    private final BoardManager boardManager;

    // 게시글추가
    // 회원가입이 된 멤버만 글을 쓸 수 있다는 조건
    public Board createBoard(Board board) {
        // 회원가입이 된 멤버인지 확인하기
        memberService.findVerifiedMember(board.getMember().getMemberId());

        return saveBoard(board);
    }


    /*
    질문수정 구현내용
    - 등록된 질문의 제목과 내용은 질문을 등록한 회원(고객)만 수정할 수 있어야 한다.0
    - 회원이 등록한 질문을 비밀글로 변경할 경우, QUESTION_SECRET 상태로 수정되어야 한다.
    - 질문 상태 중에서 QUESTION_ANSWERED 로의 변경은 관리자만 가능하다.
    - 회원이 등록한 질문을 회원이 삭제할 경우, QUESTION_DELETE 상태로 수정되어야 한다.
    - 답변 완료된 질문은 수정할 수 없다.
    */

    // 게시글 업데이트

    public Board updateBoard(Board board){ // 보드에서 업데이트하라는 요청이 왔을 때 받아주는 역할
        // 세부동작들은 클래스를 하나 만들어주고 하는게 좋음 (코드의 가독성과 재사용을 위해)
        // 여기서는 검증을 다 해놓고 Manager한테 보내주자

        Board updatedBoard = boardManager.boardUpdater(board);
        //업데이트 된 보드를 리턴받아야함
        return saveBoard(updatedBoard);



    }
    // 게시글을 저장하기
    private Board saveBoard(Board board){
        return boardRepository.save(board);
    }



    // 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
    public void cancelOrder(Long boardId) {
        Board findBoard = findVerifiedBoard(boardId);
        findBoard.setQuestionStatus(Board.QuestionStatus.QUESTION_DELETE);
        //boardRepository.delete(deleteBoard);
    }
}
