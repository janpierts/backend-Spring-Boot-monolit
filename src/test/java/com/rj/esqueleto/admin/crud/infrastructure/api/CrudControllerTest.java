package com.rj.esqueleto.admin.crud.infrastructure.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.esqueleto.EsqueletoApplication;
import com.rj.esqueleto.admin.crud.application.dto.InsertUpdate_Crud_Model;
import com.rj.esqueleto.admin.crud.application.service.Crud_Service;
import com.rj.esqueleto.admin.crud.infrastructure.api.CrudController;

// @WebMvcTest(controllers = CrudController.class)
// @org.springframework.test.context.ContextConfiguration(classes = CrudControllerTest.MiniConfig.class)
// class CrudControllerTest {
//     @org.springframework.boot.SpringBootConfiguration
//     @org.springframework.boot.autoconfigure.EnableAutoConfiguration(
//         exclude = {
//             org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//             org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
//         }
//     )
//     static class MiniConfig {}
//     @Autowired
//     private MockMvc mockMvc;
//     @MockitoBean
//     private Crud_Service crudService;
//     @Autowired
//     private ObjectMapper objectMapper;

//     private final String BASE_URL = "/api/v1/crud-entities";

//     @Test
//     // @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
//     void createEntity_WhenSuccess() throws Exception{
//         String repositoryType = "inMysqlAdapter_JPA";
//         InsertUpdate_Crud_Model dtoInput = new InsertUpdate_Crud_Model(null, "Test Name","test_email@mail.mail");
//         Map<String, Object> mockServiceResponse = new HashMap<>();
//         mockServiceResponse.put("state",1);
//         mockServiceResponse.put("message","Registro exitoso");
//         when(crudService.save_Crud_Entity(eq(repositoryType), any(InsertUpdate_Crud_Model.class))).thenReturn(mockServiceResponse);

//         mockMvc.perform(post(BASE_URL+"/{repositoryType}/create",repositoryType)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(dtoInput)))
//             .andExpect(status().isCreated())
//             .andExpect(jsonPath("$.state").value(1))
//             .andExpect(jsonPath("$.message").value("Registro exitoso"));
//     }
// }

@WebMvcTest(
    controllers = CrudController.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"
    }
)
@org.springframework.test.context.ContextConfiguration(classes = EsqueletoApplication.class)
class CrudControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private Crud_Service crudService;
    
    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/v1/crud-entities";

    @Test
    void createEntity_WhenSuccess() throws Exception {
        String repositoryType = "inMysqlAdapter_JPA";
        InsertUpdate_Crud_Model dtoInput = new InsertUpdate_Crud_Model(null, "Test Name","test_email@mail.mail");
        Map<String, Object> mockServiceResponse = new HashMap<>();
        mockServiceResponse.put("state", 1);
        mockServiceResponse.put("message", "Registro exitoso");
        when(crudService.save_Crud_Entity(eq(repositoryType), any(InsertUpdate_Crud_Model.class))).thenReturn(mockServiceResponse);

        mockMvc.perform(post(BASE_URL + "/{repositoryType}/create", repositoryType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInput)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.state").value(1))
            .andExpect(jsonPath("$.message").value("Registro exitoso"));
    }
}

/*

@WebMvcTest(
    controllers = CrudController.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration"
    }
)
@org.springframework.test.context.ContextConfiguration(classes = CrudControllerTest.MiniConfig.class)
class CrudControllerTest {

    @org.springframework.boot.SpringBootConfiguration
    static class MiniConfig {}

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private Crud_Service crudService;
    
    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/v1/crud-entities";

    @Test
    void createEntity_WhenSuccess() throws Exception {
        String repositoryType = "inMysqlAdapter_JPA";
        InsertUpdate_Crud_Model dtoInput = new InsertUpdate_Crud_Model(null, "Test Name","test_email@mail.mail");
        Map<String, Object> mockServiceResponse = new HashMap<>();
        mockServiceResponse.put("state", 1);
        mockServiceResponse.put("message", "Registro exitoso");
        when(crudService.save_Crud_Entity(eq(repositoryType), any(InsertUpdate_Crud_Model.class))).thenReturn(mockServiceResponse);

        mockMvc.perform(post(BASE_URL + "/{repositoryType}/create", repositoryType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInput)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.state").value(1))
            .andExpect(jsonPath("$.message").value("Registro exitoso"));
    }
}
*/