package com.QnaApi.board.service;

import com.QnaApi.board.entity.Board;
import com.QnaApi.board.repository.BoardRepository;
import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import com.QnaApi.member.entity.Member;
import com.QnaApi.member.repository.MemberRepository;
import com.QnaApi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// 빈 검색으로 잡히게 하겠다, autowired대상이 되게 하겠다
@Component
@RequiredArgsConstructor
public class BoardManager {
    // private final BoardService boardService; 양방향에서 주입할수가 x 그래서 매니저에서는 서비스를 생성자주입을 하면 안됨
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;




    // 최종적으로 게시글을 업데이트해서 service한테 리턴시켜주는 updater 구현로직
    public Board boardUpdater(Board board) {
        // board를 작성한 멤버가 실제로 있는지 없는지 확인하기
        Member findMember = memberService.findVerifiedMember(board.getMember().getMemberId());

        // (board)게시글이 존재하는지 확인하는 로직
        Board findBoard = findVerifiedBoard(board.getBoardId()); // FindBoard은 원본게시글이 저장됨

        //게시글을 등록한 멤버Id와 업데이트를 요청 멤버의 id가 같은지 확인하기
        checkNotExistBoard(board);

        // 게시글의 상태가 답변완료인 경우 게시글을 수정할수없도록 체크함
        checkQuestionAnswered(board);

        // repository.board의 title, content, status(공개/ 비공개)로 수정하기
        return changeContent(board);
    }

    //boardId로 게시글이 존재하는지 확인하기
    public Board findVerifiedBoard(Long boardId){
        Optional<Board> optionalBoard = boardRepository.findById(boardId); // boardRepository에서 뭘 찾을건데 BYid로 찾을거야 지금 전달받은 board의 Id로 보드를 찾을거야
        Board findBoard =
            optionalBoard.orElseThrow(()-> // Optional클래스의 null값이 NPE에러를 발생시키지 않도록 하는 예외처리로직작성
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND)); // boardId로 게시글을 찾을 수 없다면 예외처리를 함
        return findBoard;
    }

    // 게시글의 답변상태가 '답변완료'인지 확인하는 로직
    // (1) QUESTION_ANSWERED(답변완료) 질문상태는 변경하지 못하도록 막는 로직이 필요함
    public void checkQuestionAnswered(Board board){
        if (board.getQuestionStatus().equals(Board.QuestionStatus.QUESTION_ANSWERED)) {
           throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ANSWERED_QUESTION);
        }
    }

    //(2) 등록된 질문의 제목과 내용은 질문을 등록한 회원(고객)만 수정,삭제,조회할 수 있어야 한다.
    public void checkNotExistBoard(Board board){// 업데이트할 내용이 저장되어있는 board라고 가정
        Long customerMemberId = board.getMember().getMemberId(); //업데이트를 요청한 멤버의 아이디를 변수에 저장
        Member findMember = memberService.findVerifiedMember(customerMemberId); // 멤버아이디를 통해 memberRepository에 저장된 멤버객체를 가져옴
        List<Board> boardList = findMember.getBoards(); // 업데이트를 요청한 멤버(findMember)가 작성했던 bordList들을 전부 가져옴

        // 업데이트,삭제,조회를 요청한 회원이 작성한 게시글들 중에 수정,삭제,조회 하고자하는 게시글의 id가 있는지 for문을 통해 확인
        boolean existBoard = false;
        for(Board b:boardList){
            if(board.getBoardId() == b.getBoardId()){ //업데이트를 요청한 멤버의 게시글목록중 업데이트요청 게시글의 boardId가 있는지 확인
                existBoard = true; // 있다면 true
                break; // 찾았으니 종료!
            }
        }
        if(!existBoard){ // existBoard==false라면 (업데이트를 요청한 멤버가 쓴 게시글이 아니었다면
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_BOARD); // 게시글을 찾을 수 없다고 예외를 던져라
        }
    }



    // (1),(2)가 통과되었다는 가정하의 동작이 실행해야함
    public Board changeContent(Board board){
        Board findBoard = findVerifiedBoard(board.getBoardId());

        Optional.ofNullable(board.getTitle())
                .ifPresent(findBoard::setTitle);
        Optional.ofNullable(board.getContent())
                .ifPresent(findBoard::setContent);
        Optional.ofNullable(board.getContentStatus())
                .ifPresent(findBoard::setContentStatus);

        return findBoard;
    }

    public Board delteContent(Board board){
        Member findMember = memberService.findVerifiedMember(board.getMember().getMemberId());
        Board findBoard = findVerifiedBoard(board.getBoardId()); // FindBoard은 원본게시글이 저장됨
        checkNotExistBoard(board);
//        - 이미 삭제 상태인 질문은 삭제할 수 없다.
        if(findBoard.getQuestionStatus()==Board.QuestionStatus.QUESTION_DELETE){
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_BOARD);
        }

//        - 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
        findBoard.setQuestionStatus(Board.QuestionStatus.QUESTION_DELETE);
        return findBoard;
    }
}
