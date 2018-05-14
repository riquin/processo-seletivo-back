package com.processo.seletivo.domain.instituicao;

import com.processo.seletivo.SeletivoApplication;
import com.processo.seletivo.core.exception.CustomDependencyException;
import com.processo.seletivo.core.exception.CustomInternalException;
import com.processo.seletivo.core.exception.CustomNotFoundException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasToString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeletivoApplication.class)
@WebAppConfiguration
@ContextConfiguration
public class InstituicaoControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    InstituicaoRepository instituicaoRepository;

   Instituicao instituicaoValida, instituicaoInvalida;
    private MockMvc mockMvc;
    private List<Instituicao> instituicaoList;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RestAssuredMockMvc.mockMvc(mockMvc);

        this.instituicaoList = new ArrayList<>();

         instituicaoValida = new Instituicao();
         instituicaoValida.setNome("Instituicao 1");
         instituicaoValida.setCodigo("AB1");
         instituicaoValida.setNumeroFiscal("9876");
        instituicaoValida.setBairro("Universitario");
        instituicaoValida.setLogradouro("Avenida Universitária");
        instituicaoValida.setNumero("144587");
        instituicaoValida.setCaixaPostal("745415575");
        instituicaoValida.setPais("Brasil");
        instituicaoValida.setProvincia("Goias");
        instituicaoValida.setMunicipio("Anapolis");

        this.instituicaoList.add(instituicaoValida);

        instituicaoInvalida = new Instituicao();
        instituicaoInvalida.setNome(RandomStringUtils.randomAlphabetic(90));
        instituicaoInvalida.setCodigo(RandomStringUtils.randomAlphabetic(10));
        instituicaoInvalida.setBairro(RandomStringUtils.randomAlphabetic(50));
        instituicaoInvalida.setLogradouro(RandomStringUtils.randomAlphabetic(90));
        instituicaoInvalida.setNumero(RandomStringUtils.randomAlphabetic(10));
        instituicaoInvalida.setCaixaPostal(RandomStringUtils.randomAlphabetic(20));
        instituicaoInvalida.setPais(RandomStringUtils.randomAlphabetic(30));
        instituicaoInvalida.setProvincia(RandomStringUtils.randomAlphabetic(30));
        instituicaoInvalida.setProvincia(RandomStringUtils.randomAlphabetic(30));

        this.instituicaoList.add(instituicaoInvalida);
    }

    @After
    public void finish() throws Exception {
    }

    @Test
    public void listarComListaVazia() throws Exception {
        List<Instituicao> list = new ArrayList<>();

        when(instituicaoRepository.findAll()).thenReturn(list);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/instituicao")
                .then()
                .statusCode(200)
                .body(String.valueOf("content".toString()), hasToString("[]"));
    }

    @Test
    public void cadastrarItemComDadosValidos() {
        when(instituicaoRepository.save(instituicaoValida)).thenReturn(instituicaoValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(instituicaoValida)
                .when()
                .post("/instituicao")
                .then()
                .statusCode(200);
    }

    @Test
    public void cadastrarItemComDadosInvalidos() {
        doThrow(CustomInternalException.class).when(instituicaoRepository).save(instituicaoInvalida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(instituicaoInvalida)
                .when()
                .post("/instituicao")
                .then()
                .statusCode(400);
    }

    @Test
    public void cadastrarItemComDadosDuplicados() {
        when(instituicaoRepository.findByNomeOrCodigo(instituicaoValida.getNome(), instituicaoValida.getCodigo())).thenReturn(this.instituicaoList);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(instituicaoValida)
                .when()
                .post("/instituicao")
                .then()
                .statusCode(409);
    }

    @Test
    public void alterarItemCadastradoComDadosValidos() {
        when(instituicaoRepository.save(instituicaoValida)).thenReturn(instituicaoValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(instituicaoValida)
                .when()
                .put("/instituicao")
                .then()
                .statusCode(200);
    }

    @Test
    public void alterarItemCadastradoComDadosInvalidos() {
        doThrow(CustomInternalException.class).when(instituicaoRepository).save(instituicaoInvalida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(instituicaoInvalida)
                .when()
                .put("/instituicao")
                .then()
                .statusCode(400);
    }

    @Test
    public void alterarItemCadastradoComDadosDuplicados() {
        instituicaoValida.setId(1);

        when(instituicaoRepository.findByIdAndNomeOrCodigo(instituicaoValida.getId(), instituicaoValida.getNome(), instituicaoValida.getCodigo())).thenReturn(instituicaoList);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(instituicaoValida)
                .when()
                .put("/instituicao")
                .then()
                .statusCode(409);
    }

    @Test
    public void listarComListaCheia() {
        when(instituicaoRepository.findAll()).thenReturn(this.instituicaoList);

        List<Instituicao> response = io.restassured.module.mockmvc.RestAssuredMockMvc
                .when()
                .get("/instituicao")
                .then()
                .statusCode(200)
                .body("content.codigo", hasItems("AB1"))
                .extract()
                .path("content");
    }

    @Test
    public void buscarItemExistentePeloID() {
        when(instituicaoRepository.findOne(instituicaoValida.getId())).thenReturn(instituicaoValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/instituicao/{id}", instituicaoValida.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void buscarItemInexistentePeloID() {
        when(instituicaoRepository.findOne(instituicaoValida.getId())).thenThrow(CustomNotFoundException.class);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/instituicao/{id}", instituicaoValida.getId())
                .then()
                .statusCode(404);
    }

    @Test
    public void alterarItemInexistente() {
        when(instituicaoRepository.findOne(instituicaoValida.getId())).thenThrow(CustomNotFoundException.class);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/instituicao/alterar/{id}", instituicaoValida.getId())
                .then()
                .statusCode(404);
    }

    @Test
    public void excluirItemComDependencia() {
        when(instituicaoRepository.findOne(instituicaoValida.getId())).thenReturn(instituicaoValida);
        doThrow(CustomDependencyException.class).when(instituicaoRepository).delete(instituicaoValida.getId());

        io.restassured.module.mockmvc.RestAssuredMockMvc.delete("/instituicao/{id}", instituicaoValida.getId())
                .then()
                .statusCode(412);
    }

    @Test
    public void excluirItemCadastrado() {
        when(instituicaoRepository.findOne(instituicaoValida.getId())).thenReturn(instituicaoValida);
        doNothing().when(instituicaoRepository).delete(instituicaoValida.getId());
        io.restassured.module.mockmvc.RestAssuredMockMvc.delete("/instituicao/{id}", instituicaoValida.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void excluirItemInexistente() {
        when(instituicaoRepository.findOne(instituicaoValida.getId())).thenThrow(CustomNotFoundException.class);
        doNothing().when(instituicaoRepository).delete(instituicaoValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.delete("/instituicao/{id}", instituicaoValida.getId())
                .then()
                .statusCode(404);
    }
}
