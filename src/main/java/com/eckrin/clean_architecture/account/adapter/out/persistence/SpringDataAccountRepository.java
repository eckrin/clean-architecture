package com.eckrin.clean_architecture.account.adapter.out.persistence;

import com.eckrin.clean_architecture.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAccountRepository extends JpaRepository<Account, Long> {
}
