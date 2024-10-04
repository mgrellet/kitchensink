package com.gl.kitchensink.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response {
  private String message;
  private int status;
  private Object data;
}
