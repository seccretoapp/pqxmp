package com.seccreto.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class InstantUtils {

  private InstantUtils() {
  }
  
  public static Instant toInstant(Date date) {
    return Instant.ofEpochMilli(date.getTime());
  }

  public static Instant now() {
    return Instant.now().truncatedTo(ChronoUnit.MICROS);
  }
}
