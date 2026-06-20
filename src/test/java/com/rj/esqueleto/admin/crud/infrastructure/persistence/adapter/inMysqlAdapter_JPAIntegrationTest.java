package com.rj.esqueleto.admin.crud.infrastructure.persistence.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.rj.esqueleto.admin.crud.application.dto.InsertMulti_Crud_Model;
import com.rj.esqueleto.admin.crud.application.dto.InsertUpdate_Crud_Model;
import com.rj.esqueleto.admin.crud.domain.model.Crud_Entity;
import com.rj.esqueleto.admin.crud.domain.readmodel.Crud_multiReadModel;
import com.rj.esqueleto.admin.crud.infrastructure.persistence.springdata.crudSpringDataRepository;

@SpringBootTest
@ActiveProfiles("test")
@EntityScan(basePackages = "com.rj.esqueleto.admin.crud.infrastructure.persistence.entity")
class inMysqlAdapter_JPAIntegrationTest {
    @Autowired
    private inMysqlAdapter_JPA adapter;
    @Autowired
    private crudSpringDataRepository jpaRepository;
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @BeforeEach
    void setUp() {
        jpaRepository.deleteAll();
    }

    @Test
    void save_Crud_Entity_ShouldPersistSuccessfully() {
        InsertUpdate_Crud_Model input = new InsertUpdate_Crud_Model(null, "Ruddy Correa", "ruddy@example.com");
        Crud_Entity saved = adapter.save_Crud_Entity(input);
        assertNotNull(saved.getId());
        assertEquals("Ruddy Correa", saved.getName());
        assertEquals("ruddy@example.com", saved.getEmail());
        Optional<Crud_Entity> found = adapter.find_Crud_EntityByName("Ruddy Correa");
        assertTrue(found.isPresent());
    }

    @Test
    void save_Crud_Entity_DuplicateName_ShouldThrowException() {
        InsertUpdate_Crud_Model input1 = new InsertUpdate_Crud_Model(null, "Carlos Perez", "carlos@example.com");
        adapter.save_Crud_Entity(input1);
        InsertUpdate_Crud_Model input2 = new InsertUpdate_Crud_Model(null, "Carlos Perez", "otro@example.com");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adapter.save_Crud_Entity(input2);
        });
        assertTrue(exception.getMessage().contains("ya existe en la base de datos"));
    }

    @Test
    void update_Crud_Entity_ShouldModifyExistingRecord() {   
        Crud_Entity initial = adapter.save_Crud_Entity(new InsertUpdate_Crud_Model(null, "Original", "old@mail.com"));
        InsertUpdate_Crud_Model updateModel = new InsertUpdate_Crud_Model(initial.getId(), "Modificado", "new@mail.com");
        Crud_Entity updated = adapter.update_Crud_Entity(updateModel);
        assertEquals("Modificado", updated.getName());
        assertEquals("new@mail.com", updated.getEmail());
    }

    @Test
    void delete_Crud_Entity_logical_ById_ShouldSetStateToFalse() {
        Crud_Entity entity = adapter.save_Crud_Entity(new InsertUpdate_Crud_Model(null, "Borrado Logico", "logic@mail.com"));
        Crud_Entity deleted = adapter.delete_Crud_Entity_logical_ById(entity.getId());
        assertFalse(deleted.getState());
    }

    @Test
    void save_multi_Crud_Entity_ShouldHandleValidAndDuplicates() {
        adapter.save_Crud_Entity(new InsertUpdate_Crud_Model(null, "Duplicado", "dup@mail.com"));
        List<InsertMulti_Crud_Model> list = List.of(
            new InsertMulti_Crud_Model("Nuevo 1", "nuevo1@mail.com", true, "OK"),
            new InsertMulti_Crud_Model("Duplicado", "dup@mail.com", true, "OK"),
            new InsertMulti_Crud_Model("Invalido", "bad@mail.com", false, "Formato incorrecto")
        );

        List<Crud_multiReadModel> results = adapter.save_multi_Crud_Entity(list);
        assertEquals(3, results.size());
        assertTrue(results.stream().anyMatch(r -> r.name().equals("Nuevo 1") && r.isValid()));
        assertTrue(results.stream().anyMatch(r -> r.name().equals("Duplicado") && !r.isValid()));
        assertTrue(results.stream().anyMatch(r -> r.name().equals("Invalido") && !r.isValid()));
    }

    @Test
    void save_Crud_Entity_JPA_SP_ShouldExecuteStoredProcedure() {
        InsertUpdate_Crud_Model input = new InsertUpdate_Crud_Model(null, "SP User", "sp@example.com");
        Crud_Entity saved = adapter.save_Crud_Entity_JPA_SP(input);
        assertNotNull(saved.getId());
        assertEquals("SP User", saved.getName());
        assertTrue(saved.getState());
        assertNotNull(saved.getCreated());
    }

    @Test
    void find_Crud_Entity_JPA_SP_ById_ShouldReturnEntity() {
        Crud_Entity saved = adapter.save_Crud_Entity(new InsertUpdate_Crud_Model(null, "Search SP", "search@sp.com"));
        Optional<Crud_Entity> found = adapter.find_Crud_Entity_JPA_SP_ById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Search SP", found.get().getName());
    }
    
    @Test
    void save_multi_Crud_Entity_JPA_SP_ShouldExecuteJsonBulkInsert() {
        List<InsertMulti_Crud_Model> list = List.of(
            new InsertMulti_Crud_Model("SP Multi 1", "sp1@mail.com", true, "OK"),
            new InsertMulti_Crud_Model("SP Multi 2", "sp2@mail.com", true, "OK")
        );

        List<Crud_multiReadModel> results = adapter.save_multi_Crud_Entity_JPA_SP(list);
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(r -> r.name().equals("SP Multi 1") && r.isValid()));
    }
}
