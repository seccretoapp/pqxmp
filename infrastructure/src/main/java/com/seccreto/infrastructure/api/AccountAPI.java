package com.seccreto.infrastructure.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("account")
public interface AccountAPI {

  // ====================== GET ======================

  @GetMapping(
    value = "{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<?> getById(@PathVariable(name = "id") String id);
}
