package com.moa.moa_server.domain.auth.repository;

import com.moa.moa_server.domain.auth.entity.OAuth;
import com.moa.moa_server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface OAuthRepository extends JpaRepository<OAuth, Long> {

    Optional<OAuth> findById(@NonNull String id);
    void deleteByUserId(Long userId);
    Optional<OAuth> findByUser(User user);
}
