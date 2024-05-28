package com.eckrin.clean_architecture.account.adapter.out.persistence;

import com.eckrin.clean_architecture.account.adapter.out.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataAccountRepository extends JpaRepository<AccountEntity, Long> {
}
