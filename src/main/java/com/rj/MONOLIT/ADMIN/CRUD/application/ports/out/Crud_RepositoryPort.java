package com.rj.MONOLIT.ADMIN.CRUD.application.ports.out;

import java.util.List;
import java.util.Optional;
import com.rj.MONOLIT.ADMIN.CRUD.application.dto.InsertMulti_Crud_Model;
import com.rj.MONOLIT.ADMIN.CRUD.application.dto.InsertUpdate_Crud_Model;
import com.rj.MONOLIT.ADMIN.CRUD.application.dto.SearchRequest;
import com.rj.MONOLIT.ADMIN.CRUD.domain.model.Crud_Entity;
import com.rj.MONOLIT.ADMIN.CRUD.domain.readmodel.Crud_multiReadModel;

public interface Crud_RepositoryPort {
    Crud_Entity save_Crud_Entity(InsertUpdate_Crud_Model entity);
    Crud_Entity save_Crud_Entity_JDBC_SP(InsertUpdate_Crud_Model entity);
    Crud_Entity save_Crud_Entity_JPA_SP(InsertUpdate_Crud_Model entity);
    List<Crud_multiReadModel> save_multi_Crud_Entity(List<InsertMulti_Crud_Model> entityList);
    List<Crud_multiReadModel> save_multi_Crud_Entity_JDBC_SP(List<InsertMulti_Crud_Model> entityList);
    List<Crud_multiReadModel> save_multi_Crud_Entity_JPA_SP(List<InsertMulti_Crud_Model> entityList);
    List<Crud_multiReadModel> save_import_Crud_Entity(List<InsertMulti_Crud_Model>entityList);
    List<Crud_multiReadModel> save_import_Crud_Entity_JDBC_SP(List<InsertMulti_Crud_Model>entityList);
    List<Crud_multiReadModel> save_import_Crud_Entity_JPA_SP(List<InsertMulti_Crud_Model>entityList);
    Optional<Crud_Entity> find_Crud_EntityById(Long id);
    Optional<Crud_Entity> find_Crud_Entity_JDBC_SP_ById(Long id);
    Optional<Crud_Entity> find_Crud_Entity_JPA_SP_ById(Long id);
    Optional<Crud_Entity> find_Crud_EntityByName(String name);
    Optional<Crud_Entity> find_Crud_Entity_JDBC_SP_ByName(String name);
    Optional<Crud_Entity> find_Crud_Entity_JPA_SP_ByName(String name);
    Optional<List<Crud_Entity>> find_Crud_EntityByNames(List<SearchRequest> names);
    Optional<List<Crud_Entity>> find_Crud_Entity_JDBC_SP_ByNames(List<SearchRequest> names);
    Optional<List<Crud_Entity>> find_Crud_Entity_JPA_SP_ByNames(List<SearchRequest> names);
    List<Crud_Entity> findAll_Crud_entity();
    List<Crud_Entity> findAll_Crud_entity_JDBC_SP();
    List<Crud_Entity> findAll_Crud_entity_JPA_SP();
    Crud_Entity update_Crud_Entity(InsertUpdate_Crud_Model entity);
    Crud_Entity update_Crud_Entity_JDBC_SP(InsertUpdate_Crud_Model entity);
    Crud_Entity update_Crud_Entity_JPA_SP(InsertUpdate_Crud_Model entity);
    void delete_Crud_Entity_phisical_ById(Long id);
    void delete_Crud_Entity_phisical_JDBC_SP_ById(Long id);
    void delete_Crud_Entity_phisical_JPA_SP_ById(Long id);
    Crud_Entity delete_Crud_Entity_logical_ById(Long id);
    Crud_Entity delete_Crud_Entity_logical_JDBC_SP_ById(Long id);
    Crud_Entity delete_Crud_Entity_logical_JPA_SP_ById(Long id);
}
