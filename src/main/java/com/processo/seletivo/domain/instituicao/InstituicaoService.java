package com.processo.seletivo.domain.instituicao;

import com.processo.seletivo.core.exception.CustomDuplicatedException;
import com.processo.seletivo.core.exception.ExceptionMessageCode;
import com.processo.seletivo.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstituicaoService extends AbstractService<Instituicao> {
    @Autowired
    InstituicaoRepository instituicaoRepository;

    @Override
    public Instituicao save(Instituicao instituicao){
        if(instituicao.getId() == 0){
            if(instituicaoRepository.findByNomeOrCodigo(instituicao.getNome(), instituicao.getCodigo()).isEmpty()){
                return instituicaoRepository.save(instituicao);
            }
        }else if(instituicaoRepository.findByIdAndNomeOrCodigo(instituicao.getId(), instituicao.getNome(), instituicao.getCodigo()).isEmpty()){
            return instituicaoRepository.save(instituicao);
        }
        throw new CustomDuplicatedException(ExceptionMessageCode.MENSAGEM_REGISTRO_DUPLICADO);
    }
}
