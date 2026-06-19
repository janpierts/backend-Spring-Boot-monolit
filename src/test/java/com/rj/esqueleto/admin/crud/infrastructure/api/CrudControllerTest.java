package com.rj.esqueleto.admin.crud.infrastructure.api;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.esqueleto.admin.crud.application.dto.InsertUpdate_Crud_Model;
import com.rj.esqueleto.admin.crud.application.service.Crud_Service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CrudController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class CrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Crud_Service crudService;

    @Test
    void testSuccessInsert_Crud_Entity() throws Exception {
        String repositoryType = "inMysqlAdapter_JPA";//inMysqlAdapter_JPA,inMemoryRepository
        InsertUpdate_Crud_Model dtoInput = new InsertUpdate_Crud_Model(null, "Test Name Ruddy", "TestJanpierts@Description");
        
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("state", 1);
        serviceResponse.put("message", "Entidad creada con éxito");
        serviceResponse.put("id", 100);
        Mockito.when(crudService.save_Crud_Entity(Mockito.eq(repositoryType), Mockito.any(InsertUpdate_Crud_Model.class)))
               .thenReturn(serviceResponse);

        mockMvc.perform(post("/api/v1/crud-entities/{repositoryType}/create", repositoryType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInput)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.state").value(1))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.message").value("Entidad creada con éxito"));
    }

    @Test
    void testFailInsert_Crud_Entity() throws Exception {
        String repositoryType = "inMysqlAdapter_JPA";//inMysqlAdapter_JPA,inMemoryRepository
        InsertUpdate_Crud_Model inputModel = new InsertUpdate_Crud_Model(null,"Test", "test@example.com");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("state", -1);
        errorResponse.put("message", "Error interno al procesar la entidad");

        Mockito.when(crudService.save_Crud_Entity(Mockito.eq(repositoryType), Mockito.any(InsertUpdate_Crud_Model.class)))
               .thenReturn(errorResponse);

        mockMvc.perform(post("/api/v1/crud-entities/{repositoryType}/create", repositoryType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputModel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.state").value(-1))
                .andExpect(jsonPath("$.message").value("Error interno al procesar la entidad"));
    }
}