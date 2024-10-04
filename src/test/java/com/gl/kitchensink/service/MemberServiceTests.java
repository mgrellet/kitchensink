package com.gl.kitchensink.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gl.kitchensink.entity.Member;
import com.gl.kitchensink.repository.MemberRepository;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MemberServiceTests {

  @InjectMocks
  private MemberService memberService;

  @Mock
  private MemberRepository memberRepository;

  List<Member> mockMemberList;
  Member mockMember;

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.openMocks(this);

    mockMember = Member.builder()
        .id(1L)
        .name("John Doe")
        .email("johndoe@example.com")
        .phoneNumber("12345678912")
        .build();
    mockMemberList = List.of(mockMember);
  }

  @Test
  void registerUserTest() {

    when(memberRepository.save(any(Member.class))).thenReturn(mockMember);

    var mockMemberParam = Member.builder()
        .name("John Doe")
        .email("johndoe@example.com")
        .phoneNumber("12345678912")
        .build();
    var result = memberService.register(mockMemberParam);

    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(mockMember.getEmail(), result.getEmail()));
  }

  @Test
  void registerUserTest_ExistingMember() {

    when(memberRepository.findAll()).thenReturn(mockMemberList);

    var mockMemberParam = Member.builder()
        .name("John Doe")
        .email("johndoe@example.com")
        .phoneNumber("12345678912")
        .build();

    assertThrows(ValidationException.class, () -> memberService.register(mockMemberParam));
  }

  @Test
  void getMemberByIdTest() {
    when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(mockMember));
    var result = memberService.findById(1L);
    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(mockMember.getEmail(), result.getEmail()));
  }

  @Test
  void getMemberNotExistTest() {
    when(memberRepository.findById(2L)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> memberService.findById(2L));
  }

  @Test
  void getMembersTest() {
    when(memberRepository.findAll()).thenReturn(mockMemberList);
    var result = memberService.findAll();
    assertAll("Check results",
        () -> assertNotNull(result),
        () -> assertEquals(mockMemberList.getFirst().getEmail(), result.getFirst().getEmail()));
  }
}
