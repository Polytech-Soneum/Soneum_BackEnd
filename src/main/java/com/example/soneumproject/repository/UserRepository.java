package com.example.soneumproject.repository;

import com.example.soneumproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    boolean existsById (String userId);

    Optional<Users> findByUserID (String userId);
}
