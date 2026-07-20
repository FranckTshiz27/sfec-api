package com.rawsur.apidgi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rawsur.apidgi.converters.IntermediaryConverter;
import com.rawsur.apidgi.dto.dgi.intermediairy.CreateIntermediaryDTO;
import com.rawsur.apidgi.dto.dgi.intermediairy.UpdateIntermediaryDTO;
import com.rawsur.apidgi.models.dgi.Intermediary;
import com.rawsur.apidgi.routes.Routes;
import com.rawsur.apidgi.services.dgi.IntermediaryService;
import com.rawsur.apidgi.services.dgi.UserIntermediaryService;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Routes.INTERMEDIAIRY_URI)
public class IntermediaryController {

  @Autowired
  private UserIntermediaryService userIntermediaryService;

  @Autowired
  private IntermediaryService intermediaryService;

  /**
   * @return
   */
  @GetMapping
  public ResponseEntity<List<Intermediary>> getAll() {
    List<Intermediary> interms = new ArrayList<>();
    try {
      interms = this.intermediaryService.findByAll();
    } catch (Exception e) {
    }
    // List<Intermediary>
    return ResponseEntity.ok().body(interms);
  }

  /**
   * @param intermediary
   * @return
   */
  @PostMapping
  public ResponseEntity<Intermediary> create(@RequestBody @Valid CreateIntermediaryDTO intermediaryDTO) {
    Intermediary intermediary = this.intermediaryService.create(intermediaryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(intermediary);
  }

  /**
   * @param client
   * @return
   */
  @PutMapping
  public ResponseEntity<Intermediary> update(@RequestBody @Valid UpdateIntermediaryDTO intermediaryDTO) {
    Intermediary intermediary = IntermediaryConverter.convertUpdateIntermediaryDtoToIntermediary(intermediaryDTO);
    Intermediary savedIntermediary = this.intermediaryService.update(intermediary);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedIntermediary);
  }

}
