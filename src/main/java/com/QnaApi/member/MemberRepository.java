package com.QnaApi.member;

import com.QnaApi.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Member findById(Long memberId);
}
