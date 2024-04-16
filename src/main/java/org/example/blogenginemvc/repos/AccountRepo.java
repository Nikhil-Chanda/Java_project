package org.example.blogenginemvc.repos;

import java.util.List;
import java.util.Optional;

import org.example.blogenginemvc.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long>{
    Optional<Account> findOneByEmail(String email);
    List<Account> findByEmail(String email);
}
