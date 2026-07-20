package com.rawsur.apidgi.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rawsur.apidgi.dto.dgi.entreprise.CreateEntrepriseDto;
import com.rawsur.apidgi.dto.dgi.entreprise.EditEntrepriseDto;
import com.rawsur.apidgi.models.dgi.Entreprise;
import com.rawsur.apidgi.routes.Routes;
import com.rawsur.apidgi.services.dgi.EntrepriseService;

@RestController
@RequestMapping(Routes.ENTREPRISE_BASE_URI)
public class EntrepriseController {
    @Autowired
    private EntrepriseService entrepriseService;

    @PostMapping
    public ResponseEntity<Entreprise> create(@RequestBody @Valid CreateEntrepriseDto createEntrepriseDto) {
        Entreprise createdEntreprise = entrepriseService.create(createEntrepriseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntreprise);
    }

    @GetMapping
    public ResponseEntity<List<Entreprise>> getAll() {
        List<Entreprise> entreprises = entrepriseService.findAll();
        return ResponseEntity.ok(entreprises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entreprise> getById(@PathVariable UUID id) {
        Entreprise entreprise = entrepriseService.findById(id);
        return ResponseEntity.ok(entreprise);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Entreprise> update(@PathVariable UUID id,
            @RequestBody @Valid EditEntrepriseDto editEntrepriseDto) {
        if (!id.equals(editEntrepriseDto.getId())) {
            throw new RuntimeException("L'ID dans le path ne correspond pas a l'ID dans le body");
        }
        Entreprise updatedEntreprise = entrepriseService.update(editEntrepriseDto);
        return ResponseEntity.ok(updatedEntreprise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        entrepriseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
