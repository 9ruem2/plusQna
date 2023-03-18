package com.QnaApi.board;

import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.Member;
import com.QnaApi.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

// 빈 검색으로 잡히게 하겠다, autowired대상이 되게 하겠다
@Component
@RequiredArgsConstructor
public class BoardManager {
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final MemberService memberService;


    // 답변이 완료된 질문인지 확인하는 로직 필요 -> 완료됐으면 수정하면 안되게 해야함
    // 게시글을 등록한 멤버Id와 업데이트를 요청 멤버의 아이디가 같은지 확인하는 로직
    // (1),(2) 조건을 만족한다면?
    // 공개질문/ 비공개질문 상태 수정할 수 있어야함
    // QUESTION_ANSWERED(답변완료) 질문상태는 변경하지 못하도록 막는 로직
    // 질문을 삭제한경우 ? -> 이 로직이 질문수정 구현에 포함되어야하나? 아무튼 삭제되면 QUESTION_DELETE상태로 수정되어야함


    public Board boardUpdater(Board board) {
        // 멤버가 있는지 없는지 확인하는 로직
        Member findMember = memberService.findVerifiedMember(board.getMember().getMemberId());

        // (board)게시글이 존재하는지?
        Board findBoard = findVerifiedBoard(board.getBoardId());
//        Optional.ofNullable(board.getQuestionStatus())
//                .ifPresent(boardStatus -> findBoard.setQuestionStatus(boardStatus));
        return findBoard;
    }


    // 작성된 게시글의 회원정보를 확인하는 로직 -> 없어도 될것같음

    //boardId로 게시글이 존재하는지 확인
    public Board findVerifiedBoard(Long boardId){
        Optional<Board> optionalBoard = boardRepository.findById(boardId); // repository에서 boardId를 가진 해당 Board객체를 리턴값으로 가져옴
        Board findBoard =
                optionalBoard.orElseThrow(()-> // Optional클래스의 null값이 NPE에러를 발생시키지 않도록 하는 예외처리로직작성
                        new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND)); // boardId로 게시글을 찾을 수 없다면 예외처리를 함
        return findBoard;
    }


}
