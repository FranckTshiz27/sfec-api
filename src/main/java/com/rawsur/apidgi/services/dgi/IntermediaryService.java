package com.rawsur.apidgi.services.dgi;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rawsur.apidgi.converters.IntermediaryConverter;
import com.rawsur.apidgi.dto.dgi.intermediairy.CreateIntermediaryDTO;
import com.rawsur.apidgi.enums.IntermediaryType;
import com.rawsur.apidgi.models.dgi.Intermediary;
import com.rawsur.apidgi.models.dgi.User;
import com.rawsur.apidgi.repositories.dgi.IntermediaryRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IntermediaryService {

  @Autowired
  private final IntermediaryRepo intermediaryRepo;

  @Autowired
  private final UserIntermediaryService userIntermediaryService;

  @Autowired
  private KeycloakTools kcTools;

  @Transactional
  public Intermediary create(CreateIntermediaryDTO intermediaryDTO) {
    User user = this.kcTools.getCurrentUser();
    Intermediary intermediary = new Intermediary();

    IntermediaryType intermediaryType = IntermediaryConverter.typegetFromChaine(intermediaryDTO.getType());
    intermediary.setIntermediaryType(intermediaryType.getValue().trim());
    intermediary.setName(intermediaryDTO.getName().trim());
    String code = intermediaryDTO.getCode().trim();
    intermediary.setCode(code);

    intermediary = this.intermediaryRepo.save(intermediary);

    return intermediary;

  }

  @Transactional
  public Intermediary update(Intermediary intermediaryIn) {
    User user = this.kcTools.getCurrentUser();

    intermediaryIn.getCode().trim();
    intermediaryIn.getIntermediaryType().trim();
    intermediaryIn.getName().trim();

    Intermediary savedIntermediaryIn = this.intermediaryRepo.save(intermediaryIn);
    return savedIntermediaryIn;
  }

  public Intermediary getByCode(String code) {
    return this.intermediaryRepo.findByCode(code);
  }

  @Transactional
  public Intermediary findById(UUID id) {
    return this.intermediaryRepo.findById(id).orElse(null);
  }

  @Transactional
  public List<Intermediary> findByAll() {

    List<Intermediary> intermediaries = this.intermediaryRepo.findAll();

    return intermediaries;
  }

}
