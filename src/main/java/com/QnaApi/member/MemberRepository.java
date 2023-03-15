package com.QnaApi.member;

import com.QnaApi.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
