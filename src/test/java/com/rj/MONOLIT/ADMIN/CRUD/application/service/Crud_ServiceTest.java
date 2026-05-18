package com.rj.MONOLIT.ADMIN.CRUD.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.rj.MONOLIT.ADMIN.CRUD.application.dto.InsertUpdate_Crud_Model;
import com.rj.MONOLIT.ADMIN.CRUD.application.ports.out.Crud_RepositoryPort;
import com.rj.MONOLIT.ADMIN.CRUD.domain.model.Crud_Entity;
import com.rj.MONOLIT.COMMON.utils.helperEndpoints;

@ExtendWith(MockitoExtension.class)
class Crud_ServiceTest {
    @Mock
    private Map<String, Crud_RepositoryPort> crudRepositoryPortMap;
    
    @Mock
    private Crud_RepositoryPort crudRepositoryPort;
    
    @Spy
    private helperEndpoints helper = new helperEndpoints();
    
    @InjectMocks
    private Crud_Service crudService;

    @Test
    void insertSimpleSuccessEntity(){
        InsertUpdate_Crud_Model dtoInput = new InsertUpdate_Crud_Model(null, "Test Name", "data.test@email.dev");
        Crud_Entity testReturnEntity = new Crud_Entity();
        testReturnEntity.setId(99L);
        testReturnEntity.setName("Test Name");
        testReturnEntity.setEmail("data.test@email.dev");
        testReturnEntity.setCreated(LocalDateTime.now());
        testReturnEntity.setState(true);

        Map<String, Object> respuestaEsperada = new HashMap<>();
        respuestaEsperada.put("state", 1);
        respuestaEsperada.put("message", "Registro exitoso");
        respuestaEsperada.put("successBody", testReturnEntity);
        when(crudRepositoryPortMap.get("inMysqlAdapter_JPA")).thenReturn(crudRepositoryPort);
        when(crudRepositoryPort.save_Crud_Entity(dtoInput)).thenReturn(testReturnEntity);
        Object result = crudService.save_Crud_Entity("inMysqlAdapter_JPA", dtoInput);
        assertNotNull(result,"The result never be null");
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertEquals(1,resultMap.get("state"));
        assertEquals("Registro exitoso",resultMap.get("message"));
        assertEquals(testReturnEntity,resultMap.get("successBody"));
        verify(crudRepositoryPort, times(1)).save_Crud_Entity(dtoInput);
    }

    @Test
    void updateSimpleSuccessEntity(){
        InsertUpdate_Crud_Model dtoInput = new InsertUpdate_Crud_Model(1L, "Test Name", "data.test@email.dev");
        Crud_Entity testReturnEntity = new Crud_Entity();
        testReturnEntity.setId(1L);
        testReturnEntity.setName("Test Name");
        testReturnEntity.setEmail("data.test@email.dev");
        testReturnEntity.setCreated(LocalDateTime.now());
        testReturnEntity.setState(true);

        Map<String, Object> respuestaEsperada = new HashMap<>();
        respuestaEsperada.put("state", 1);
        respuestaEsperada.put("message", "Registro exitoso");
        respuestaEsperada.put("updateBody", testReturnEntity);
        when(crudRepositoryPortMap.get("inMysqlAdapter_JPA")).thenReturn(crudRepositoryPort);
        when(crudRepositoryPort.update_Crud_Entity(dtoInput)).thenReturn(testReturnEntity);
        Object result = crudService.update_Crud_Entity("inMysqlAdapter_JPA", dtoInput);
        assertNotNull(result,"The result never be null");
        Map<?, ?> resultMap = (Map<?, ?>) result;
        assertEquals(1,resultMap.get("state"));
        assertEquals("Actualización exitosa",resultMap.get("message"));
        assertEquals(testReturnEntity,resultMap.get("updateBody"));
        verify(crudRepositoryPort, times(1)).update_Crud_Entity(dtoInput);
    }
}
