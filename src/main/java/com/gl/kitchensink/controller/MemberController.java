package com.gl.kitchensink.controller;

import com.gl.kitchensink.dto.Response;
import com.gl.kitchensink.entity.Member;
import com.gl.kitchensink.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/rest/members")
public class MemberController {

  MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @Operation(summary = "Create a new member")
  @ApiResponse(responseCode = "201", description = "Member created successfully",
      content = @Content(mediaType = "application/json"))
  @ApiResponse(responseCode = "500", description = "Internal server error",
      content = @Content(mediaType = "application/json"))
  @PostMapping
  public ResponseEntity<?> createMember(@RequestBody Member member) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/")
        .buildAndExpand(member.getEmail())
        .toUri();

    try {
      Response response = Response.builder()
          .data(memberService.register(member))
          .message("success")
          .status(HttpStatus.CREATED.value())
          .build();
      return ResponseEntity.created(location).body(response);

    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while registering member")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

  @Operation(summary = "Gets the list of Members")
  @ApiResponse(responseCode = "200", description = "Member retrieved successfully",
      content = @Content(mediaType = "application/json"))
  @ApiResponse(responseCode = "500", description = "Internal server error",
      content = @Content(mediaType = "application/json"))
  @GetMapping
  public ResponseEntity<?> listAllMembers() {
    try {
      Response response = Response.builder()
          .data(memberService.findAll())
          .message("success")
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while fetching member")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }

  }

  @Operation(summary = "Gets Member by id")
  @ApiResponse(responseCode = "200", description = "Member retrieved successfully",
      content = @Content(mediaType = "application/json"))
  @ApiResponse(responseCode = "404", description = "Not Found",
      content = @Content(mediaType = "application/json"))
  @ApiResponse(responseCode = "500", description = "Internal server error",
      content = @Content(mediaType = "application/json"))
  @GetMapping("/{id}")
  public ResponseEntity<?> lookupMemberById(@PathVariable("id") Long id) {
    try {
      Response response = Response.builder()
          .data(memberService.findById(id))
          .message("success")
          .status(HttpStatus.OK.value())
          .build();
      return ResponseEntity.ok().body(response);
    } catch (NoSuchElementException e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.NOT_FOUND.value())
          .message(e.getMessage())
          .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(errorResponse);
    } catch (Exception e) {
      Response errorResponse = Response.builder()
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .data(e.getMessage())
          .message("Error while fetching member")
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorResponse);
    }
  }

}
