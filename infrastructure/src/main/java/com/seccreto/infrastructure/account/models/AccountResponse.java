package com.seccreto.infrastructure.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AccountResponse(
  @JsonProperty("id") String id,
  @JsonProperty("public_key") String publicKey,
  @JsonProperty("signature") String signature,
  @JsonProperty("is_active") Boolean isActive,
  @JsonProperty("created_at") Instant createdAt,
  @JsonProperty("updated_at") Instant updatedAt,
  @JsonProperty("deleted_at") Instant deletedAt
) {
}
