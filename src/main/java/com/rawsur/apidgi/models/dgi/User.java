package com.rawsur.apidgi.models.dgi;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "user_entity")
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private UUID id;

  @Column(name = "keycloak_id", unique = true, nullable = false)
  private String keycloakId;

  private String fname;
  private String lname;
  private String username;
  @Column(name = "generated_password")
  private String generatedPassword;
  @Column(name = "frappe_access_key")
  private String frappeAccessKey;

  @CreationTimestamp
  private Date createdat;

  @UpdateTimestamp
  private Date updatedat;

   @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private List<UserInterm> intermediairies = new ArrayList<>();

    @Override
    public String toString() {
        return "";
    }
}
