package com.processo.seletivo.domain.unidade;

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

public class UnidadeControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    UnidadeRepository unidadeRepository;

    Unidade unidadeValida, unidadeInvalida;
    private MockMvc mockMvc;
    private List<Unidade> unidadeList;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RestAssuredMockMvc.mockMvc(mockMvc);

        this.unidadeList = new ArrayList<>();

        unidadeValida = new Unidade();
        unidadeValida.setNome("unidade 1");
        unidadeValida.setCodigo("123");
        unidadeValida.setNumeroFiscal("");


        this.unidadeList.add(unidadeValida);

        unidadeInvalida = new Unidade();
        unidadeInvalida.setNome(RandomStringUtils.randomAlphabetic(90));
        unidadeInvalida.setCodigo(RandomStringUtils.randomAlphabetic(10));


        this.unidadeList.add(unidadeInvalida);
    }

    @After
    public void finish() throws Exception {
    }

    @Test
    public void listarComListaVazia() throws Exception {
        List<Unidade> list = new ArrayList<>();

        when(unidadeRepository.findAll()).thenReturn(list);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/unidade")
                .then()
                .statusCode(200)
                .body(String.valueOf("content".toString()), hasToString("[]"));
    }

    @Test
    public void cadastrarItemComDadosValidos() {
        when(unidadeRepository.save(unidadeValida)).thenReturn(unidadeValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(unidadeValida)
                .when()
                .post("/unidade")
                .then()
                .statusCode(200);
    }

    @Test
    public void cadastrarItemComDadosInvalidos() {
        doThrow(CustomInternalException.class).when(unidadeRepository).save(unidadeInvalida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(unidadeInvalida)
                .when()
                .post("/unidade")
                .then()
                .statusCode(400);
    }

    @Test
    public void cadastrarItemComDadosDuplicados() {
        when(unidadeRepository.findByNomeOrCodigo(unidadeValida.getNome(), unidadeValida.getCodigo())).thenReturn(this.unidadeList);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(unidadeValida)
                .when()
                .post("/unidade")
                .then()
                .statusCode(409);
    }

    @Test
    public void alterarItemCadastradoComDadosValidos() {
        when(unidadeRepository.save(unidadeValida)).thenReturn(unidadeValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(unidadeValida)
                .when()
                .put("/unidade")
                .then()
                .statusCode(200);
    }

    @Test
    public void alterarItemCadastradoComDadosInvalidos() {
        doThrow(CustomInternalException.class).when(unidadeRepository).save(unidadeInvalida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(unidadeInvalida)
                .when()
                .put("/unidade")
                .then()
                .statusCode(400);
    }

    @Test
    public void alterarItemCadastradoComDadosDuplicados() {
        unidadeValida.setId(1);

        when(unidadeRepository.findByIdAndNomeOrCodigo(unidadeValida.getId(), unidadeValida.getNome(), unidadeValida.getCodigo())).thenReturn(unidadeList);

        io.restassured.module.mockmvc.RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(unidadeValida)
                .when()
                .put("/unidade")
                .then()
                .statusCode(409);
    }

    @Test
    public void listarComListaCheia() {
        when(unidadeRepository.findAll()).thenReturn(this.unidadeList);

        List<Unidade> response = io.restassured.module.mockmvc.RestAssuredMockMvc
                .when()
                .get("/unidade")
                .then()
                .statusCode(200)
                .body("content.codigo", hasItems("123"))
                .extract()
                .path("content");
    }

    @Test
    public void buscarItemExistentePeloID() {
        when(unidadeRepository.findOne(unidadeValida.getId())).thenReturn(unidadeValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/unidade/{id}", unidadeValida.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void buscarItemInexistentePeloID() {
        when(unidadeRepository.findOne(unidadeValida.getId())).thenThrow(CustomNotFoundException.class);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/unidade/{id}", unidadeValida.getId())
                .then()
                .statusCode(404);
    }

    @Test
    public void alterarItemInexistente() {
        when(unidadeRepository.findOne(unidadeValida.getId())).thenThrow(CustomNotFoundException.class);

        io.restassured.module.mockmvc.RestAssuredMockMvc.get("/unidade/alterar/{id}", unidadeValida.getId())
                .then()
                .statusCode(404);
    }

    @Test
    public void excluirItemComDependencia() {
        when(unidadeRepository.findOne(unidadeValida.getId())).thenReturn(unidadeValida);
        doThrow(CustomDependencyException.class).when(unidadeRepository).delete(unidadeValida.getId());

        io.restassured.module.mockmvc.RestAssuredMockMvc.delete("/unidade/{id}", unidadeValida.getId())
                .then()
                .statusCode(412);
    }

    @Test
    public void excluirItemCadastrado() {
        when(unidadeRepository.findOne(unidadeValida.getId())).thenReturn(unidadeValida);
        doNothing().when(unidadeRepository).delete(unidadeValida.getId());

        io.restassured.module.mockmvc.RestAssuredMockMvc.delete("/unidade/{id}", unidadeValida.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void excluirItemInexistente() {
        when(unidadeRepository.findOne(unidadeValida.getId())).thenThrow(CustomNotFoundException.class);
        doNothing().when(unidadeRepository).delete(unidadeValida);

        io.restassured.module.mockmvc.RestAssuredMockMvc.delete("/unidade/{id}", unidadeValida.getId())
                .then()
                .statusCode(404);
    }
}
