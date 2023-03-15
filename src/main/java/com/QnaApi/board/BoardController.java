package com.QnaApi.board;

import com.QnaApi.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1/boards")
@Validated
@Slf4j
@RequiredArgsConstructor //final이 붙어있는 애들만 빈으로 인식 필요한 애들만 생성자를 만들어줌 따라서 컨트롤러에서는 @RAC~~
public class BoardController {
    private final BoardMapper mapper; //의존성주입때문에 스프링 컨테이너가 매퍼랑 보드서비스를 빈의 형태로 가지고 있는건데 어떤객체에 연결을 해줘야할지 몰라서 에러가 나고있는 상황
    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity postBoard(@Valid @RequestBody BoardPostDto boardPostDto){
        Board board = boardService.createBoard(mapper.boardPostDtoToBoard(boardPostDto));
        return new ResponseEntity<>(
                mapper.BoardToBoardResponseDto(board), HttpStatus.CREATED);

    }
/*
질문수정 구현내용
- 등록된 질문의 제목과 내용은 질문을 등록한 회원(고객)만 수정할 수 있어야 한다.
- 회원이 등록한 질문을 비밀글로 변경할 경우, QUESTION_SECRET 상태로 수정되어야 한다.
- 질문 상태 중에서 QUESTION_ANSWERED 로의 변경은 관리자만 가능하다.
- 회원이 등록한 질문을 회원이 삭제할 경우, QUESTION_DELETE 상태로 수정되어야 한다.
- 답변 완료된 질문은 수정할 수 없다.
*/
    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(@PathVariable("board-id") @Positive Long board,
                                     @Valid @RequestBody BoardPatchDto boardPatchDto)
    Board board = boardService.updateBoard(mapper.boardPatchDtoToBoard(boardPatchDto))
    }
/*
질문조회 구현
- 1건의 특정 질문은 회원(고객)과 관리자 모두 조회할 수 있다.
- 비밀글 상태인 질문은 질문을 등록한 회원(고객)과 관리자만 조회할 수 있다.
- 1건의 질문 조회 시, 해당 질문에 대한 답변이 존재한다면 답변도 함께 조회되 어야 한다.
- 이미 삭제 상태인 질문은 조회할 수 없다.
 */

    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id") @Positive Long boardId){
        Board board = boardService.findVerifiedBoard(boardId);
        BoardResponseDto boardResponseDto= mapper.BoardToBoardResponseDto(board);
        return new ResponseEntity<>(boardResponseDto,HttpStatus.OK);
    }
/*
질문삭제 구현
- 1건의 질문은 회원(고객)만 삭제할 수 있다.
- 1건의 질문 삭제는 질문을 등록한 회원만 가능하다.
- 질문 삭제 시, 테이블에서 row 자체가 삭제되는 것이 아니라 질문 상태 값이 (QUESTION_DELETE)으로 변경되어야 한다.
- 이미 삭제 상태인 질문은 삭제할 수 없다.
 */
    @DeleteMapping("/{board-id}")
    public ResponseEntity cancelOrder(@PathVariable("board-id") @Positive Long boardId){
        boardService.cancelOrder(boardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
