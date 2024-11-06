package com.seccreto.infrastructure.account.persistence;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRedisRepository extends PagingAndSortingRepository<AccountRedisEntity, String> {
  AccountRedisEntity findById(String id);
}
