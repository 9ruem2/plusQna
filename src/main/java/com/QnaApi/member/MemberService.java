package com.QnaApi.member;

import java.util.List;

public class MemberService {
    public Member createMember(Member member){
        Member createdMember=member;
        return createdMember;
    }

    public Member updateMember(Member member){
        Member updatedMember=member;
        return updatedMember;
    }

    public Member findMember(long memberId){
        //Member member = new Member(memberId, "hgd@gmail.com", "홍길동","010-1234-1234");
       // return member;
        return null;
    }

    public List<Member> findMembers(){
        //List<Member> members = List.of(
                //new Member(1,"hgd@gmail.com","홍길동","010-1234-1234"),
                //new Member(2,"qwe@gmail.com","이길동","010-1234-5678"));
//        return members;
        return null;
    }

    public void deleteMember(long memberId){

    }
}
