package com.processo.seletivo.domain.unidade;

import com.processo.seletivo.core.exception.CustomDuplicatedException;
import com.processo.seletivo.core.exception.ExceptionMessageCode;
import com.processo.seletivo.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnidadeService extends AbstractService<Unidade> {

    @Autowired
    UnidadeRepository unidadeRepository;

    @Override
    public Unidade save(Unidade unidade) {
        if (unidade.getId() == 0) {
            if (unidadeRepository.findByNomeOrCodigo(unidade.getNome(), unidade.getCodigo()).isEmpty()) {
                return unidadeRepository.save(unidade);
            }
        } else if (unidadeRepository.findByIdAndNomeOrCodigo(unidade.getId(), unidade.getNome(), unidade.getCodigo()).isEmpty()) {
            return unidadeRepository.save(unidade);
        }

        throw new CustomDuplicatedException(ExceptionMessageCode.MENSAGEM_REGISTRO_DUPLICADO);
    }
}