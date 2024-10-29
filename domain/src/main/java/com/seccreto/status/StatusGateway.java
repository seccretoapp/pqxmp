package com.seccreto.status;

import java.util.Optional;

public interface StatusGateway {
  SystemStatusType setSystemStatus(Status status);
  Optional<Status> getSystemStatus();
}
