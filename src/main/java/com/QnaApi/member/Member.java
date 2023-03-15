package com.QnaApi.member;

import com.QnaApi.board.Board;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor //컴파일러가 필요한 생성자를 자동으로 만들어줌
public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "member")
    private List<Board> boards=new ArrayList<>();
}
