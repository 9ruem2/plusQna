package com.QnaApi.member;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberDtoToMember(MemberPostDto memberPostDto);

    MemberPostDto memberToMemberResponseDto(Member response);
}
