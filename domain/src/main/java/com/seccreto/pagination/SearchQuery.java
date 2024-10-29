package com.seccreto.pagination;

public record SearchQuery(
  int page,
  int perPage,
  String terms,
  String sort,
  String direction
) {
}
