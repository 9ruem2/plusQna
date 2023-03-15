package com.QnaApi.board;

import com.QnaApi.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Enumerated(EnumType.STRING)
    private Question_status QuestionStatus = Question_status.QUESTION_REGISTRATION;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

//    @CreatedDate //엔티티가 만들어질때 변수에 시간이 자동으로 입력됨
//    @Column(name="created_At", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name="modified_At")
//    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;


    public enum Question_status{
        QUESTION_REGISTRATION,
        QUESTION_ANSWERED,
        QUESTION_DELETE;
    }

    public enum Content_status{
        PUBLIC,
        SECRET;
    }

    public void setMember(Member member){
        if(!this.member.getBoards().contains(this)){
            this.member.getBoards().add(this);
        }
    }
}
