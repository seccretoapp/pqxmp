package com.seccreto.role;

import com.seccreto.account.AccountID;

import java.util.Optional;

public interface RoleGateway {
  Boolean hasRole(AccountID accountId, Role role);
  Optional<Role> getRole(AccountID accountId);
}
