package com.rawsur.apidgi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rawsur.apidgi.dto.dgi.user.UserIntermDto;
import com.rawsur.apidgi.dto.dgi.user.UserResponse;
import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.models.dgi.UserInterm;
import com.rawsur.apidgi.routes.Routes;
import com.rawsur.apidgi.services.dgi.KeycloakTools;
import com.rawsur.apidgi.services.dgi.UserService;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Routes.USER_URI)
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private KeycloakTools kcTools;

  @GetMapping("/list")
  public ResponseEntity<List<UserResponse>> findByAll() {
    List<UserResponse> users = this.userService.findByAll();
    return ResponseEntity.ok().body(users);
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> findCurrentUserFromToken() {
    UserResponse user = this.userService.findCurrentUserFromToken();
    return ResponseEntity.ok().body(user);
  }

  @GetMapping("v1/find-all")
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = this.userService.getUsers();
    return ResponseEntity.ok().body(users);
  }

  @GetMapping("v1/user/keycloackId")
  public ResponseEntity<User> findUserByKeycloackId() {
    User user = this.kcTools.getCurrentUser();
    return ResponseEntity.ok().body(user);
  }

  @PostMapping("create-user-intermediairies")
  public ResponseEntity<List<UserInterm>> createUserIntermediaires(@RequestBody UserIntermDto usersInterms) {
    List<UserInterm> users = this.userService.createUserInterms(usersInterms);
    return ResponseEntity.status(HttpStatus.CREATED).body(users);
  }
}
