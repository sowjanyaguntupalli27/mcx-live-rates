package com.vibullion.backend.repository;

import com.vibullion.backend.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {

    Optional<UserDetails> findByPhone(String username);
}
