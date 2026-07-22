package com.rawsur.apidgi.repositories.dgi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rawsur.apidgi.models.dgi.Intermediary;
import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.models.dgi.UserInterm;



public interface UserIntermRepo extends JpaRepository<UserInterm, UUID> {
  final String DELETE_BY_USER = "DELETE FROM public.user_interm WHERE user_id = :userID";

  public Optional<UserInterm> findByUserAndInterm(User user, Intermediary interm);

  public List<UserInterm> findByUser(User user);

  @Modifying
  @Query(value = DELETE_BY_USER, nativeQuery = true)
  public int deleteByUser(@Param("userID") UUID userID);
}
