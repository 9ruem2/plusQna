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

@RestController // @Controller와 @ResponsBody의 동작을 하나로 결합한 편의 컨트롤러, 모든 핸들러 메소드에서 @ResponseBody를 사용할 필요가 없다는 것입니다.
@RequestMapping("/v1/boards")
@Validated //유효성검사 시 필요
@Slf4j
@RequiredArgsConstructor //final이 붙어있는 애들만 빈으로 인식 필요한 애들만 생성자를 만들어줌 따라서 컨트롤러에서는 @RAC~~
public class BoardController {
    private final BoardMapper mapper; //의존성주입때문에 스프링 컨테이너가 매퍼랑 보드서비스를 빈의 형태로 가지고 있는건데 어떤객체에 연결을 해줘야할지 몰라서 에러가 나고있는 상황
    private final BoardService boardService;
    private final MemberService memberService;

// 게시글 추가
    @PostMapping
    public ResponseEntity postBoard(@Valid @RequestBody BoardPostDto boardPostDto){
        Board board = boardService.createBoard(mapper.boardPostDtoToBoard(boardPostDto));
        return new ResponseEntity<>(
                mapper.BoardToBoardResponseDto(board), HttpStatus.CREATED);
    }

    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(@PathVariable("board-id") @Positive Long board,
                                     @Valid @RequestBody BoardPatchDto boardPatchDto){
    //Dto를 mapper로 바꿔서 service로직에서 UpdateBoard()를 실행
    Board patchBoard = boardService.updateBoard(mapper.boardPatchDtoToBoard(boardPatchDto));
    return new ResponseEntity<>(mapper.BoardToBoardResponseDto(patchBoard), HttpStatus.OK);
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
