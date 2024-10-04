package com.gl.kitchensink.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gl.kitchensink.entity.Member;
import com.gl.kitchensink.service.MemberService;
import com.google.gson.Gson;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ContextConfiguration(classes = TestContext.class)
@Import(MemberController.class)
public class MemberControllerTests {

  @MockBean
  private MemberService memberService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  List<Member> mockMemberList;
  Member mockMember;

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMember = Member.builder()
        .name("John Doe")
        .email("johndoe@example.com")
        .phoneNumber("12345678912")
        .build();
    mockMemberList = List.of(mockMember);
  }

  @Test
  void registerMemberTest() throws Exception {

    when(memberService.register(any(Member.class))).thenReturn(mockMember);

    Gson gson = new Gson();
    String json = gson.toJson(mockMember);

    mockMvc.perform(post("/rest/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.name").value("John Doe"))
        .andExpect(jsonPath("$.data.email").value("johndoe@example.com"))
        .andExpect(jsonPath("$.data.phoneNumber").value("12345678912"))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(201));
  }

  @Test
  void registerMemberWithExistingEmailTest() throws Exception {
    when(memberService.register(any(Member.class))).thenThrow(
        new ValidationException("Unique Email Violation"));

    Gson gson = new Gson();
    String json = gson.toJson(mockMember);

    mockMvc.perform(post("/rest/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.data").value("Unique Email Violation"))
        .andExpect(jsonPath("$.message").value("Error while registering member"))
        .andExpect(jsonPath("$.status").value(500));
  }

  @Test
  void getMembersTest() throws Exception {

    when(memberService.findAll()).thenReturn(mockMemberList);
    mockMvc.perform(get("/rest/members"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(1))
        .andExpect(jsonPath("$.data[0].name").value("John Doe"))
        .andExpect(jsonPath("$.data[0].email").value("johndoe@example.com"))
        .andExpect(jsonPath("$.data[0].phoneNumber").value("12345678912"))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(200));
  }

  @Test
  void getMemberTest() throws Exception {

    when(memberService.findById(1L)).thenReturn(mockMember);

    mockMvc.perform(get("/rest/members/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("John Doe"))
        .andExpect(jsonPath("$.data.email").value("johndoe@example.com"))
        .andExpect(jsonPath("$.data.phoneNumber").value("12345678912"))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.status").value(200));
  }

  @Test
  void getMemberNotExistTest() throws Exception {

    doThrow(new NoSuchElementException("Member not found"))
        .when(memberService).findById(2L);

    mockMvc.perform(get("/rest/members/2"))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").value("Member not found"))
        .andExpect(jsonPath("$.status").value(404));
  }

}
