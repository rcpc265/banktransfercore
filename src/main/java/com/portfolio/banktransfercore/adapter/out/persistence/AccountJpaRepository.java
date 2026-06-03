package com.portfolio.banktransfercore.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

  Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
