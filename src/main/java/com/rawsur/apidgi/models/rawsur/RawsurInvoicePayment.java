package com.rawsur.apidgi.models.rawsur;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rawsur.apidgi.dto.dgi.sfec.SfecCertificationResponseDto;

import lombok.Data;

@Immutable
@Entity
@Table(name = "V_FACTURE_ENCAISSEMENT")
@Data
public class RawsurInvoicePayment {
    @Id
    @Column(name = "CLE")
    private String cle;

    @Column(name = "CODEINTE")
    private Integer codeInte;

    @Column(name = "CODEASSU")
    private String codeAssure;

    @Column(name = "NUMEPOLI")
    private Long numePoli;

    @Column(name = "NUMEAVEN")
    private Long numeAven;

    @Column(name = "RN")
    private String rn;

    @Column(name = "MODE_FACTURE")
    private String modeFacture;

    @Column(name = "DATEENCA")
    private LocalDateTime dateEncaissement;

    @Column(name = "DATEEFFE")
    private Date dateEffe;

    @Column(name = "DATEECHE")
    private Date dateEche;

    @Column(name = "TYPE_FACTURE")
    private String typeFacture;

    @Column(name = "CODE_ARTICLE")
    private Integer codeArticle;

    @Column(name = "TYPE_ARTICLE")
    private String typeArticle;

    @Column(name = "NAME_ARTICLE", length = 100)
    private String nameArticle;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "TAXEGROUP", length = 1)
    private String taxeGroup;

    @Column(name = "TAXSPECIFICVALUE")
    private BigDecimal taxSpecificValue;

    @Column(name = "TAXSPECIFICAMOUNT")
    private BigDecimal taxSpecificAmount;

    @Column(name = "ORIGINALPRICE", length = 1)
    private BigDecimal originalPrice;

    @Column(name = "PRICEMODIFICATION", length = 1)
    private BigDecimal priceModification;

    @Column(name = "NIF", length = 30)
    private String nif;

    @Column(name = "NOM", length = 181)
    private String nom;

    @Column(name = "CONTACT", length = 30)
    private String contact;

    @Column(name = "ADRESSE", length = 200)
    private String adresse;

    @Column(name = "GENRASSU", nullable = false)
    private String genRassu;

    @Column(name = "ID_USER")
    private String idUser;

    @Column(name = "NOM_USER", length = 60)
    private String nomUser;

    @Column(name = "TYPE_PAIEMENT")
    private String typePaiement;

    @Column(name = "AMOUNT", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "CURRENCYCODE", length = 3)
    private String currencyCode;

    @Column(name = "prim_ttc")
    private Double prime;

    @Column(name = "TVA")
    private Double tva;

    @Transient
    private String status;

    @Transient
    @JsonProperty("sfec_response")
    private SfecCertificationResponseDto sfecResponse;
}
