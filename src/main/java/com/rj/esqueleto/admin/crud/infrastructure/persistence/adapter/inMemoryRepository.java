package com.rj.esqueleto.admin.crud.infrastructure.persistence.adapter;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import com.rj.esqueleto.admin.crud.application.dto.InsertMulti_Crud_Model;
import com.rj.esqueleto.admin.crud.application.dto.InsertUpdate_Crud_Model;
import com.rj.esqueleto.admin.crud.application.dto.SearchRequest;
import com.rj.esqueleto.admin.crud.application.ports.out.Crud_RepositoryPort;
import com.rj.esqueleto.admin.crud.domain.model.Crud_Entity;
import com.rj.esqueleto.admin.crud.domain.readmodel.Crud_multiReadModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("inMemoryRepository")
public class inMemoryRepository implements Crud_RepositoryPort{
    private final List<Crud_Entity> entities = new ArrayList<>();
    private Long nextId = 1L;

    //region implemented methods

    //region save entity
    @Override
    public Crud_Entity save_Crud_Entity(InsertUpdate_Crud_Model entity) {
        
        Optional<Crud_Entity> existingEntityOpt = find_Crud_EntityByName(entity.name());
        if (existingEntityOpt.isPresent()) {
            throw new RuntimeException("Error al guardar: el nombre ya existe.");
        }
        Crud_Entity newEntity = new Crud_Entity();
        LocalDateTime now = LocalDateTime.now();
        newEntity.setName(entity.name());
        newEntity.setEmail(entity.email());
        newEntity.setId(nextId++);
        newEntity.setCreated(now);
        newEntity.setState(true);
        entities.add(newEntity);
        return newEntity;
    }
    //endregion

    //region save multiple entities
    @Override
    public List<Crud_multiReadModel> save_multi_Crud_Entity(List<InsertMulti_Crud_Model> entityList) {
        List<SearchRequest> SearchList = entityList.stream()
            .filter(InsertMulti_Crud_Model::isValid)
            .map(model -> new SearchRequest(model.name(),model.email()))
            .toList();
        List<String> existingNames = find_Crud_EntityByNames(SearchList)
            .map(list -> list.stream()
                .map(Crud_Entity::getName)
                .collect(Collectors.toList()))
            .orElse(new ArrayList<>());
        
        List<Crud_multiReadModel> readmodel = new ArrayList<>();
        if(existingNames.size() >= entityList.size()) {
            readmodel.addAll(entityList.stream()
                .filter(InsertMulti_Crud_Model::isValid)
                .map(item -> new Crud_multiReadModel(null, item.name(), item.email(), null, null, null, false, item.message())).toList());
            readmodel.addAll(entityList.stream()
                .filter(k -> k.isValid() && existingNames.contains(k.name()))
                .map(item -> new Crud_multiReadModel(null, item.name(), item.email(), null, null, null, false, "Registro ya existe en la BD"))
                .toList());
            return readmodel;
        }
        List<Crud_Entity> filteredEntities = SearchList.stream()
            .filter(entity -> !existingNames.contains(entity.name()))
            .map(model -> new Crud_Entity(null,model.name(),model.email(),null,null,null))
            .collect(Collectors.toList());
            
        for (Crud_Entity entity : filteredEntities) {
            LocalDateTime now = LocalDateTime.now();
            entity.setId(nextId++);
            entity.setCreated(now);
            entity.setState(true);
            entities.add(entity);
            readmodel.add(new Crud_multiReadModel(entity.getId(), entity.getName(), entity.getEmail(), now, null, true, true, "OK"));
        }
        readmodel.addAll(entityList.stream()
            .filter(k -> !k.isValid())
            .map(item -> new Crud_multiReadModel(null, item.name(), item.email(), null, null, null, item.isValid(), item.message()))
            .toList()
        );
        readmodel.addAll(entityList.stream()
            .filter(k -> k.isValid() && existingNames.contains(k.name()))
            .map(item -> new Crud_multiReadModel(null, item.name(), item.email(), null, null, null, false, "Registro ya existe en la BD"))
            .toList()
        );
        return readmodel;
    }
    //endregion

    //region import entities
    @Override
    @Transactional
    public List<Crud_multiReadModel> save_import_Crud_Entity(List<InsertMulti_Crud_Model> entityList) {
        return save_multi_Crud_Entity(entityList);
    }
    //endregion

    //region find entity by id and name
    @Override
    public Optional<Crud_Entity> find_Crud_EntityById(Long id) {
        Optional<Crud_Entity> result = entities.stream()
                .filter(e -> e.getId() != null && e.getId().equals(id))
                .findFirst();
        if(!result.isPresent()) throw new RuntimeException("El identificador ingresado no existe");
        return result;
    }
    //endregion
    
    //region find entity by name and name
    @Override
    public Optional<Crud_Entity> find_Crud_EntityByName(String name) {
        return entities.stream()
                .filter(e -> e.getName() != null && e.getName().equals(name))
                .findFirst();
    }
    //endregion

    //region find entities by names
    @Override
    public Optional<List<Crud_Entity>> find_Crud_EntityByNames(List<SearchRequest> names) {
        List<Crud_Entity> result = entities.stream()
        .filter(e -> e.getName() != null && 
                names.stream().anyMatch(n -> n.name().equals(e.getName())))
        .collect(Collectors.toList());

        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }
    //endregion

    //region find all entities
    @Override
    public List<Crud_Entity> findAll_Crud_entity() {
        return new ArrayList<>(entities);
    }
    //endregion

    //region update entity
    @Override
    public Crud_Entity update_Crud_Entity(InsertUpdate_Crud_Model entity) {
        Optional<Crud_Entity> existingEntityOpt = find_Crud_EntityById(entity.id()).filter(a -> Boolean.TRUE.equals(a.getState()));
        if (existingEntityOpt.isEmpty()) {
            throw new RuntimeException("Entidad CRUD no encontrada con ID: " + entity.id());
        }
        Crud_Entity existingEntity = existingEntityOpt.get();
        Crud_Entity updatedEntity = new Crud_Entity();
        delete_Crud_Entity_phisical_ById(entity.id());
        updatedEntity.setId(existingEntity.getId());
        updatedEntity.setName(entity.name());
        updatedEntity.setEmail(entity.email());
        updatedEntity.setCreated(existingEntity.getCreated());
        updatedEntity.setUpdated(LocalDateTime.now());
        updatedEntity.setState(existingEntity.getState());
        entities.add(updatedEntity);
        return updatedEntity;
    }
    //endregion

    //region delete entity phisical and logical
    @Override
    public void delete_Crud_Entity_phisical_ById(Long id) {
        entities.removeIf(e -> e.getId() != null && e.getId().equals(id));
    }
    //endregion
    
    //region delete entity logical
    @Override
    public Crud_Entity delete_Crud_Entity_logical_ById(Long id) {
        Optional<Crud_Entity> existingEntityOpt = find_Crud_EntityById(id).filter(a -> Boolean.TRUE.equals(a.getState()));
        if (existingEntityOpt.isEmpty()) {
            throw new RuntimeException("El identificador mencionado no existe o se encuntra eliminado/anulado, Id: "+id);
        }
        Crud_Entity existingEntity = existingEntityOpt.get();
        delete_Crud_Entity_phisical_ById(id);
        existingEntity.setUpdated(LocalDateTime.now());
        existingEntity.setState(false);
        entities.add(existingEntity);
        return existingEntity;
    }
    //endregion
    
    //endregion

    //region unimplemented methods
    @Override
    public Crud_Entity save_Crud_Entity_JDBC_SP(InsertUpdate_Crud_Model entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save_Crud_Entity_JDBC_SP'");
    }
    @Override
    public Crud_Entity save_Crud_Entity_JPA_SP(InsertUpdate_Crud_Model entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save_Crud_Entity_JPA_SP'");
    }
    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JDBC_SP_ById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'find_Crud_Entity_JDBC_SP_ById'");
    }
    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JPA_SP_ById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'find_Crud_Entity_JPA_SP_ById'");
    }
    @Override
    public List<Crud_Entity> findAll_Crud_entity_JDBC_SP() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll_Crud_entity_JDBC_SP'");
    }
    @Override
    public List<Crud_Entity> findAll_Crud_entity_JPA_SP() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll_Crud_entity_JPA_SP'");
    }
    @Override
    public Crud_Entity update_Crud_Entity_JDBC_SP(InsertUpdate_Crud_Model entity) {
        throw new UnsupportedOperationException("Unimplemented method 'update_Crud_Entity_JDBC_SP'");
    }
    @Override
    public Crud_Entity update_Crud_Entity_JPA_SP(InsertUpdate_Crud_Model entity) {
        throw new UnsupportedOperationException("Unimplemented method 'update_Crud_Entity_JPA_SP'");
    }
    @Override
    public void delete_Crud_Entity_phisical_JDBC_SP_ById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete_Crud_Entity_phisical_JDBC_SP_ById'");
    }
    @Override
    public void delete_Crud_Entity_phisical_JPA_SP_ById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete_Crud_Entity_phisical_JPA_SP_ById'");
    }
    @Override
    public Crud_Entity delete_Crud_Entity_logical_JDBC_SP_ById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete_Crud_Entity_logical_JDBC_SP_ById'");
    }
    @Override
    public Crud_Entity delete_Crud_Entity_logical_JPA_SP_ById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete_Crud_Entity_logical_JPA_SP_ById'");
    }
    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JDBC_SP_ByName(String name){
        throw new UnsupportedOperationException("Unimplemented method 'find_Crud_Entity_JDBC_SP_ByName'");
    }
    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JPA_SP_ByName(String name){
        throw new UnsupportedOperationException("Unimplemented method 'find_Crud_Entity_JPA_SP_ByName'");
    }
    @Override
    public List<Crud_multiReadModel> save_multi_Crud_Entity_JDBC_SP(List<InsertMulti_Crud_Model> entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save_multi_Crud_Entity_JDBC_SP'");
    }
    @Override
    public List<Crud_multiReadModel> save_multi_Crud_Entity_JPA_SP(List<InsertMulti_Crud_Model> entity) {
        throw new UnsupportedOperationException("Unimplemented method 'save_multi_Crud_Entity_JPA_SP'");
    }
    @Override
    public Optional<List<Crud_Entity>> find_Crud_Entity_JDBC_SP_ByNames(List<SearchRequest> names) {
        throw new UnsupportedOperationException("Unimplemented method 'find_Crud_Entity_JDBC_SP_ByNames'");
    }
    @Override
    public Optional<List<Crud_Entity>> find_Crud_Entity_JPA_SP_ByNames(List<SearchRequest> names) {
        throw new UnsupportedOperationException("Unimplemented method 'find_Crud_Entity_JPA_SP_ByNames'");
    }
    @Override
    public List<Crud_multiReadModel> save_import_Crud_Entity_JDBC_SP(List<InsertMulti_Crud_Model> entityList) {
        throw new UnsupportedOperationException("Unimplemented method 'save_import_Crud_Entity_JDBC_SP'");
    }
    @Override
    public List<Crud_multiReadModel> save_import_Crud_Entity_JPA_SP(List<InsertMulti_Crud_Model> entityList) {
        throw new UnsupportedOperationException("Unimplemented method 'save_import_Crud_Entity_JPA_SP'");
    }
    //endregion
}
