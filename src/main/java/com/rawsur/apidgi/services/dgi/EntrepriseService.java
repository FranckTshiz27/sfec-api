package com.rawsur.apidgi.services.dgi;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rawsur.apidgi.converters.EntrepriseConverter;
import com.rawsur.apidgi.dto.dgi.entreprise.CreateEntrepriseDto;
import com.rawsur.apidgi.dto.dgi.entreprise.EditEntrepriseDto;
import com.rawsur.apidgi.models.dgi.Entreprise;
import com.rawsur.apidgi.repositories.dgi.EntrepriseRepo;

@Service
public class EntrepriseService {
    @Autowired
    private EntrepriseRepo entrepriseRepo;

    @Transactional
    public Entreprise create(CreateEntrepriseDto createEntrepriseDto) {
        String code = createEntrepriseDto.getCode().trim().toUpperCase();
        String nif = createEntrepriseDto.getNif().trim().toUpperCase();
        if (entrepriseRepo.existsByCode(code)) {
            throw new RuntimeException("Une entreprise avec le code '" + code + "' existe deja");
        }
        if (entrepriseRepo.existsByNif(nif)) {
            throw new RuntimeException("Une entreprise avec le nif '" + nif + "' existe deja");
        }

        Entreprise entreprise = EntrepriseConverter.toEntreprise(createEntrepriseDto);
        return entrepriseRepo.save(entreprise);
    }

    @Transactional
    public List<Entreprise> findAll() {
        return entrepriseRepo.findAll();
    }

    @Transactional
    public Entreprise findById(UUID id) {
        return entrepriseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvee avec l'ID: " + id));
    }

    @Transactional
    public Entreprise update(EditEntrepriseDto editEntrepriseDto) {
        Entreprise existingEntreprise = entrepriseRepo.findById(editEntrepriseDto.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Entreprise non trouvee avec l'ID: " + editEntrepriseDto.getId()));

        String code = editEntrepriseDto.getCode().trim().toUpperCase();
        String nif = editEntrepriseDto.getNif().trim().toUpperCase();
        if (entrepriseRepo.existsByCodeAndIdNot(code, editEntrepriseDto.getId())) {
            throw new RuntimeException("Une autre entreprise avec le code '" + code + "' existe deja");
        }
        if (entrepriseRepo.existsByNifAndIdNot(nif, editEntrepriseDto.getId())) {
            throw new RuntimeException("Une autre entreprise avec le nif '" + nif + "' existe deja");
        }

        Entreprise entreprise = EntrepriseConverter.toEntreprise(editEntrepriseDto);
        entreprise.setId(existingEntreprise.getId());
        return entrepriseRepo.save(entreprise);
    }

    @Transactional
    public void delete(UUID id) {
        if (!entrepriseRepo.existsById(id)) {
            throw new RuntimeException("Entreprise non trouvee avec l'ID: " + id);
        }
        entrepriseRepo.deleteById(id);
    }
}
