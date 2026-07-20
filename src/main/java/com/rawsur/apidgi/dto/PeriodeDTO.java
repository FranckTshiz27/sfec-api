package com.rawsur.apidgi.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PeriodeDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
