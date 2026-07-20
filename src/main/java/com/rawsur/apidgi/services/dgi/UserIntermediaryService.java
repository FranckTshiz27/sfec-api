package com.rawsur.apidgi.services.dgi;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.models.dgi.UserInterm;
import com.rawsur.apidgi.repositories.dgi.UserIntermRepo;
import com.rawsur.apidgi.repositories.dgi.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserIntermediaryService {

  @Autowired
  private final UserIntermRepo userIntermRepo;
  @Autowired
  private final UserRepo userRepo;

  @Transactional
  public List<UserInterm> getUserIntermediairiesByUSer(String username) {
    User userDb = this.userRepo.findByUsername(username);
    return this.userIntermRepo.findByUser(userDb);
  }

}
