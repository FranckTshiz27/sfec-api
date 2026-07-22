package com.rawsur.apidgi.repositories.dgi;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rawsur.apidgi.models.dgi.Intermediary;

import java.util.List;
import java.util.UUID;

public interface IntermediaryRepo extends JpaRepository<Intermediary, UUID> {

  final String findAllByUser = "SELECT i.* FROM public.user_interm ui INNER JOIN public.intermediary i ON i.id = ui.interm_id INNER JOIN public.user_entity u ON u.id = ui.user_id WHERE u.id = :userID";

  @Query("FROM intermediary where code = :key ")
  public Intermediary findByKey(String key);
   
  public Intermediary findByCode(String code);

  @Query(value = findAllByUser, nativeQuery = true)
  public List<Intermediary> findAllByUser(@Param("userID") UUID userID);

}
