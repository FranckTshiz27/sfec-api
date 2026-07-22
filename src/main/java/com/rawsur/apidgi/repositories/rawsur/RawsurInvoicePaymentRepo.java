package com.rawsur.apidgi.repositories.rawsur;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rawsur.apidgi.models.rawsur.RawsurInvoicePayment;


public interface RawsurInvoicePaymentRepo extends JpaRepository<RawsurInvoicePayment, String> {
    final String FIND_CURRENT = "select * from V_FACTURE_ENCAISSEMENT "
            + "WHERE TRUNC(DATEENCA) = TRUNC(SYSDATE)-3";

    @Query(value = FIND_CURRENT, nativeQuery = true)
    List<RawsurInvoicePayment> findCurrents();

    RawsurInvoicePayment findByCle(String cle);

    @Query(value = "SELECT * FROM V_FACTURE_ENCAISSEMENT "
            + "WHERE TRUNC(DATEENCA) >= TRUNC(:startDate) "
            + "AND TRUNC(DATEENCA) <= TRUNC(:endDate)", nativeQuery = true)
    List<RawsurInvoicePayment> findByDateEncaBetween(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM V_FACTURE_ENCAISSEMENT "
            + "WHERE TRUNC(DATEENCA) >= TRUNC(:startDate) "
            + "AND TRUNC(DATEENCA) <= TRUNC(:endDate) "
            + "AND CODEINTE IN (:codes)", nativeQuery = true)
    List<RawsurInvoicePayment> findByDateEncaBetweenAndCodeInteIn(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("codes") Collection<Integer> codes);

    @Query(value = "SELECT * FROM V_FACTURE_ENCAISSEMENT "
            + "WHERE (:codeInte IS NULL OR CODEINTE = :codeInte) "
            + "AND (:numePoli IS NULL OR NUMEPOLI = :numePoli) "
            + "AND (:numeAven IS NULL OR NUMEAVEN = :numeAven)", nativeQuery = true)
    List<RawsurInvoicePayment> findByCriteria(@Param("codeInte") Integer codeInte,
            @Param("numePoli") Long numePoli,
            @Param("numeAven") Long numeAven);

    @Query(value = "SELECT * FROM V_FACTURE_ENCAISSEMENT "
            + "WHERE CODEINTE IN (:codes) "
            + "AND (:codeInte IS NULL OR CODEINTE = :codeInte) "
            + "AND (:numePoli IS NULL OR NUMEPOLI = :numePoli) "
            + "AND (:numeAven IS NULL OR NUMEAVEN = :numeAven)", nativeQuery = true)
    List<RawsurInvoicePayment> findByCriteriaRestricted(@Param("codes") Collection<Integer> codes,
            @Param("codeInte") Integer codeInte,
            @Param("numePoli") Long numePoli,
            @Param("numeAven") Long numeAven);
}

