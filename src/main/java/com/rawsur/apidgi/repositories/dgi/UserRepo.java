package com.rawsur.apidgi.repositories.dgi;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawsur.apidgi.models.dgi.User;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    public User findByUsername( String username );
}
