package com.rawsur.apidgi.models.dgi;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "intermediary")
@Data
@NoArgsConstructor
public class Intermediary implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private UUID id;

  @Column(nullable = false)
  private String code;

  @Column(name = "name", nullable = false, unique = true)
  private String name;


  @Column(name = "intermediary_type", nullable = false, length = 20)
  private String intermediaryType;

  // @OneToOne
  // @JoinColumn(name = "user_has_created", referencedColumnName = "id")
  // private User userHasCreated;

  // @OneToOne
  // @JoinColumn(name = "user_has_updated", referencedColumnName = "id")
  // private User userHasUpdated;

  @CreationTimestamp
  private Date createdat;

  @UpdateTimestamp
  private Date updatedat;

  public Intermediary(UUID id) {
    this.id = id;
  }
}
