package com.processo.seletivo.domain.unidade;

import com.processo.seletivo.domain.instituicao.Instituicao;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "unidade")
public class Unidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unidade_id_seq")
    @SequenceGenerator(name = "unidade_id_seq", sequenceName = "unidade_id_seq", allocationSize = 1)
    @Column(name = "id")
    @Getter
    @Setter
    private long id;

    @NotEmpty
    @Size(max = 80)
    @Column(name = "nome")
    @Getter
    @Setter
    private String nome;

    @NotEmpty
    @Size(max = 10)
    @Column(name = "codigo", updatable = false)
    @Getter
    @Setter
    private String codigo;

    @NotEmpty
    @Size(max = 20)
    @Column(name = "numero_fiscal")
    @Getter
    @Setter
    private String numeroFiscal;


    @Size(max = 50)
    @Column(name = "bairro")
    @Getter
    @Setter
    private String bairro;

    @Size(max = 80)
    @Column(name = "logradouro")
    @Getter
    @Setter
    private String logradouro;

    @Size(max = 10)
    @Column(name = "numero")
    @Getter
    @Setter
    private String numero;

    @Size(max = 20)
    @Column(name = "caixa_postal")
    @Getter
    @Setter
    private String caixaPostal;

    @Size(max = 30)
    @Column(name = "pais")
    @Getter
    @Setter
    private String pais;

    @Size(max = 30)
    @Column(name = "provincia")
    @Getter
    @Setter
    private String provincia;

    @Size(max = 30)
    @Column(name = "municipio")
    @Getter
    @Setter
    private String municipio;


//
//    @ManyToOne(fetch = FetchType.EAGER,  cascade=CascadeType.ALL)
//    @JoinColumn(name="id_instituicao")
//    @Getter
//    @Setter
//    private List<Instituicao> lsInstituicao;

}
