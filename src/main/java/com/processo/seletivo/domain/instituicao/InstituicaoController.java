package com.processo.seletivo.domain.instituicao;

import com.processo.seletivo.core.controller.ResponseAbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instituicao")
public class InstituicaoController extends ResponseAbstractController {

    @Autowired
    InstituicaoService instituicaoService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return jsonResponse(instituicaoService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {return jsonResponse(instituicaoService.findOne(id));}

    @PostMapping
    public ResponseEntity<?> save(@Validated @RequestBody Instituicao instituicao) {
        return jsonResponse(instituicaoService.save(instituicao));
    }

    @PutMapping
    public ResponseEntity<?> update(@Validated @RequestBody Instituicao instituicao) {
        return jsonResponse(instituicaoService.save(instituicao));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        instituicaoService.delete(id);
        return jsonResponse(null);
    }
}
