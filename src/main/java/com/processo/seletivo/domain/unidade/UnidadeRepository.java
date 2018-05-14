package com.processo.seletivo.domain.unidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    public List<Unidade> findByNomeOrCodigo(String nome, String codigo);

    public List<Unidade> findByIdAndNomeOrCodigo(long id, String nome, String codigo);
}
