package com.QnaApi.member;

import com.QnaApi.exception.BusinessLogicException;
import com.QnaApi.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    //회원가입 로직
    public Member createMember(Member member){
        // 이미 등록된 이메일인지 확인하기
        verifyExistsEmail(member.getEmail());

        return memberRepository.save(member); //repository에 저장
    }

    //등록된 이메일 확인하는 로직
    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email); //Optional<T>클래스는 해당 변수가 null값을 혹시라도 가지고 있을 경우, NPE가 발생하지 않는다.
        if(member.isPresent()) // 객체가 null이 있다면
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS); //이미 멤버가 존재한다고 클라이언트에게 알려라
    }

    //멤버정보 수정하기
    public Member updateMember(Member member){
        Member findMember = findVerifiedMember(member.getMemberId()); // 멤버 아이디를 가지고 멤버정보 가져오기

        //일단 리팩토링 전 버전으로 구현

        // 수정할 정보들이 늘어나면 반복되는 코드가 늘어나는 문제점이 있음
        // optional은 rapper클래스라고 할 수 있음
        //.ofNullable()를 통해 null이 아니면 값을 가지는 Optional객체를 생성하여 반환해주고 null이면 비어있는 optional객체를 반환함
        Optional.ofNullable(member.getName()).ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone()).ifPresent(phone -> findMember.setPhone(phone));
        // 추가된 부분
        Optional.ofNullable(member.getMemberStatus()).ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));
//        findMember.setModifiedAt(LocalDateTime.now());

        return memberRepository.save(findMember);
    }

    // 멤버 정보 가져오기
    public Member findVerifiedMember(long memberId){ //memberId를 받아옴
        // 옵셔널로도 체크할 수 있음, 멤버에 바로 접근할 때는 안전할 수 있다 옵셔널이란 것을 사용하는 이유? null값이 자주 들어올 수 있음
//        Optional<Member> optionalMember = memberRepository.findById(memberId); // memberRepository에 있는 findById메서드를 통해 멤버정보(저장되어있는거? 멤버의 모든내용)를 가져옴
//
//        Member findMember = //orElseThrow() : 저장된 값이 존재하면 그 값을 반환하고, 값이 존재하지 않으면 인수로 전달된 예외를 발생시킴.
//            optionalMember.orElseThrow(() -> //찾은 멤버가 실제로 존재하고있느냐?? 없으면 예외를 던지겠다
//                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND)); //멤버를 찾을 수 없음 예외처리
//
//        return findMember;

        // 옵셔널을 사용하지 않고 member가 Null인지 아닌지 확인하는 로직
        Member member = memberRepository.findById(memberId);
        // 이 아이디에 해당하는 member가 저장소에 있냐없냐 확인하는로직. 항상 저장소에서 값을 가지고 올때는 null일수도 아닐 수도있다는 사실을 생각해야한다.
        if(member==null){ //멤버가 널이면
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND); //예외를 던져라
        }
        return member;
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
