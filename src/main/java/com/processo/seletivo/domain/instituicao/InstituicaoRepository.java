package com.processo.seletivo.domain.instituicao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    public List<Instituicao> findByNomeOrCodigo(String nome, String codigo);

    public List<Instituicao> findByIdAndNomeOrCodigo(long id, String nome, String codigo);

}
