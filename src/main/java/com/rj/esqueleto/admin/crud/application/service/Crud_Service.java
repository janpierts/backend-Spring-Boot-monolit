package com.rj.esqueleto.admin.crud.application.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.rj.esqueleto.admin.crud.application.dto.InsertMulti_Crud_Model;
import com.rj.esqueleto.admin.crud.application.dto.InsertUpdate_Crud_Model;
import com.rj.esqueleto.admin.crud.application.dto.SearchRequest;
import com.rj.esqueleto.admin.crud.application.ports.in.Crud_ServicePort;
import com.rj.esqueleto.admin.crud.application.ports.out.Crud_RepositoryPort;
import com.rj.esqueleto.admin.crud.domain.model.Crud_Entity;
import com.rj.esqueleto.admin.crud.domain.readmodel.Crud_multiReadModel;
import com.rj.esqueleto.common.utils.helperEndpoints;

@Service
public class Crud_Service implements Crud_ServicePort {
    private final Map<String, Crud_RepositoryPort> crudRepositoryPort;

    public Crud_Service(Map<String, Crud_RepositoryPort> crudRepositoryPort) {
        this.crudRepositoryPort = crudRepositoryPort;
    }
    /*@Param typeBean: tipo de bean para usar el tipo de repositorio(adapter"inMemoryRepository,inMysqlAdapter_JDBC, inMysqlAdapter_JPA") */

    private Crud_RepositoryPort getRepositoryPort(String typeBean) {
        Crud_RepositoryPort repositoryPort = crudRepositoryPort.get(typeBean);
        if (repositoryPort == null) {
            throw new IllegalArgumentException("No repository found for type: " + typeBean);
        }
        return repositoryPort;
    }
    //region SaveSimpleEntity
    /*@Param Crud_Entity: entidad a guardar */
    @Override
    public Object save_Crud_Entity(String typeBean, InsertUpdate_Crud_Model entity) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Object result = repositoryPort.save_Crud_Entity(entity);

            return helperEndpoints.buildResponse(1, "Registro exitoso", result, null);
        }catch(IllegalArgumentException e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), null, entity);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), null, entity);
        }
    }
 
    @Override
    public Object save_Crud_Entity_JDBC_SP(String typeBean, InsertUpdate_Crud_Model entity) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Object result = repositoryPort.save_Crud_Entity_JDBC_SP(entity);

            return helperEndpoints.buildResponse(1, "Registro exitoso", result, null);
        }catch(IllegalArgumentException e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), null, entity);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), null, entity);
        }
    }

    @Override
    public Object save_Crud_Entity_JPA_SP(String typeBean, InsertUpdate_Crud_Model entity) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Object result = repositoryPort.save_Crud_Entity_JPA_SP(entity);

            return helperEndpoints.buildResponse(1, "Registro exitoso", result, null);
        }catch(IllegalArgumentException e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), null, entity);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), null, entity);
        }
    }
    //endregion

    //region SaveMultiEntity
    /*@Param List<Crud_Entity> lista de entidades a guardar */
    @Override
    public Object save_multi_Crud_Entity(String typeBean, List<InsertMulti_Crud_Model> entityList) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            List<Crud_multiReadModel> result = repositoryPort.save_multi_Crud_Entity(entityList);
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==0){
                return helperEndpoints.buildResponse(-1,"El registro masivo no se realizo", result);
            }
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==entityList.size()){
                return helperEndpoints.buildResponse(1,"Registro masivo completo", result);
            }
            List<Crud_multiReadModel> success = result.stream()
                .filter(Crud_multiReadModel::isValid)
                .collect(Collectors.toList());
            List<Crud_multiReadModel> error = result.stream()
                .filter(k -> !k.isValid())
                .collect(Collectors.toList());
            return helperEndpoints.buildResponse(0,"Solo se registraron algunos de los elementos ingresados", success, error);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entityList);
        }
    }

    @Override
    public Object save_multi_Crud_Entity_JDBC_SP(String typeBean, List<InsertMulti_Crud_Model> entityList) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            List<Crud_multiReadModel> result = repositoryPort.save_multi_Crud_Entity_JDBC_SP(entityList);
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==0){
                return helperEndpoints.buildResponse(-1,"El registro masivo no se realizo", result);
            }
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==entityList.size()){
                return helperEndpoints.buildResponse(1,"Registro masivo completo", result);
            }
            List<Crud_multiReadModel> success = result.stream()
                .filter(Crud_multiReadModel::isValid)
                .collect(Collectors.toList());
            List<Crud_multiReadModel> error = result.stream()
                .filter(k -> !k.isValid())
                .collect(Collectors.toList());
            return helperEndpoints.buildResponse(0,"Solo se registraron algunos de los elementos ingresados", success, error);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entityList);
        }
    }

    @Override
    public Object save_multi_Crud_Entity_JPA_SP(String typeBean, List<InsertMulti_Crud_Model> entityList) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            List<Crud_multiReadModel> result = repositoryPort.save_multi_Crud_Entity_JPA_SP(entityList);
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==0){
                return helperEndpoints.buildResponse(-1,"El registro masivo no se realizo", result);
            }
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==entityList.size()){
                return helperEndpoints.buildResponse(1,"Registro masivo completo", result);
            }
            List<Crud_multiReadModel> success = result.stream()
                .filter(Crud_multiReadModel::isValid)
                .collect(Collectors.toList());
            List<Crud_multiReadModel> error = result.stream()
                .filter(k -> !k.isValid())
                .collect(Collectors.toList());
            return helperEndpoints.buildResponse(0,"Solo se registraron algunos de los elementos ingresados", success, error);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entityList);
        }
    }
    //endregion

    //region SaveImportEntity
    /*@Param MultipartFile file to decode */
    @Override
    public Object save_import_Crud_Entity(String typeBean, List<InsertMulti_Crud_Model> entityList){
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            List<Crud_multiReadModel> result = repositoryPort.save_import_Crud_Entity(entityList);
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==0){
                return helperEndpoints.buildResponse(-1,"El registro masivo no se realizo", result);
            }
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==entityList.size()){
                return helperEndpoints.buildResponse(1,"Registro masivo completo", result);
            }
            List<Crud_multiReadModel> success = result.stream()
                .filter(Crud_multiReadModel::isValid)
                .collect(Collectors.toList());
            List<Crud_multiReadModel> error = result.stream()
                .filter(k -> !k.isValid())
                .collect(Collectors.toList());
            return helperEndpoints.buildResponse(0,"Solo se registraron algunos de los elementos ingresados", success, error);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entityList);
        }
    }

    @Override
    public Object save_import_Crud_Entity_JDBC_SP(String typeBean, List<InsertMulti_Crud_Model> entityList){
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            List<Crud_multiReadModel> result = repositoryPort.save_import_Crud_Entity_JDBC_SP(entityList);
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==0){
                return helperEndpoints.buildResponse(-1,"El registro masivo no se realizo", result);
            }
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==entityList.size()){
                return helperEndpoints.buildResponse(1,"Registro masivo completo", result);
            }
            List<Crud_multiReadModel> success = result.stream()
                .filter(Crud_multiReadModel::isValid)
                .collect(Collectors.toList());
            List<Crud_multiReadModel> error = result.stream()
                .filter(k -> !k.isValid())
                .collect(Collectors.toList());
            return helperEndpoints.buildResponse(0,"Solo se registraron algunos de los elementos ingresados", success, error);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entityList);
        }
    }

    @Override
    public Object save_import_Crud_Entity_JPA_SP(String typeBean, List<InsertMulti_Crud_Model> entityList){
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            List<Crud_multiReadModel> result = repositoryPort.save_import_Crud_Entity_JPA_SP(entityList);
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==0){
                return helperEndpoints.buildResponse(-1,"El registro masivo no se realizo", result);
            }
            if(result.stream().filter(Crud_multiReadModel::isValid).count()==entityList.size()){
                return helperEndpoints.buildResponse(1,"Registro masivo completo", result);
            }
            List<Crud_multiReadModel> success = result.stream()
                .filter(Crud_multiReadModel::isValid)
                .collect(Collectors.toList());
            List<Crud_multiReadModel> error = result.stream()
                .filter(k -> !k.isValid())
                .collect(Collectors.toList());
            return helperEndpoints.buildResponse(0,"Solo se registraron algunos de los elementos ingresados", success, error);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entityList);
        }
    }
    //endregion

    //region FindEntityById
    /*@Param id: entidad a buscar */
    @Override
    public Optional<Crud_Entity> find_Crud_EntityById(String typeBean, Long id) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_EntityById(id);
    }

    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JDBC_SP_ById(String typeBean, Long id) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_Entity_JDBC_SP_ById(id);
    }

    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JPA_SP_ById(String typeBean, Long id) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_Entity_JPA_SP_ById(id);
    }
    //endregion

    //region FindEntityByName
    @Override
    public Optional<Crud_Entity> find_Crud_EntityByName(String typeBean, String name) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_EntityByName(name);
    }

    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JDBC_SP_ByName(String typeBean, String name) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_Entity_JDBC_SP_ByName(name);
    }

    @Override
    public Optional<Crud_Entity> find_Crud_Entity_JPA_SP_ByName(String typeBean, String name) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_Entity_JPA_SP_ByName(name);
    }
    //endregion

    //region FindEntitiesByNames
    @Override
    public Optional<List<Crud_Entity>> find_Crud_EntityByNames(String typeBean, List<SearchRequest> names) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_EntityByNames(names);
    }

    @Override
    public Optional<List<Crud_Entity>> find_Crud_Entity_JDBC_SP_ByNames(String typeBean, List<SearchRequest> names) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_Entity_JDBC_SP_ByNames(names);
    }

    @Override
    public Optional<List<Crud_Entity>> find_Crud_Entity_JPA_SP_ByNames(String typeBean, List<SearchRequest> names) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.find_Crud_Entity_JPA_SP_ByNames(names);
    }
    //endregion

    //region FindAllEntities
    @Override
    public List<Crud_Entity> findAll_Crud_entity(String typeBean) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.findAll_Crud_entity();
    }
    
    @Override
    public List<Crud_Entity> findAll_Crud_entity_JDBC_SP(String typeBean) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.findAll_Crud_entity_JDBC_SP();
    }
    
    @Override
    public List<Crud_Entity> findAll_Crud_entity_JPA_SP(String typeBean) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        return repositoryPort.findAll_Crud_entity_JPA_SP();
    }
    //endregion

    //region UpdateEntity
    /*@Param Crud_Entity: entidad por actualizar */
    @Override
    public Object update_Crud_Entity(String typeBean, InsertUpdate_Crud_Model entity) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Crud_Entity result = repositoryPort.update_Crud_Entity(entity);
            return helperEndpoints.buildResponse(1, "Actualización exitosa", null,null,result);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entity);
        }
    }

    @Override
    public Object update_Crud_Entity_JDBC_SP(String typeBean, InsertUpdate_Crud_Model entity) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Crud_Entity result = repositoryPort.update_Crud_Entity_JDBC_SP(entity);
            return helperEndpoints.buildResponse(1, "Actualización exitosa", null,null,result);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entity);
        }
    }

    @Override
    public Object update_Crud_Entity_JPA_SP(String typeBean, InsertUpdate_Crud_Model entity) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Crud_Entity result = repositoryPort.update_Crud_Entity_JPA_SP(entity);
            return helperEndpoints.buildResponse(1, "Actualización exitosa", null,null,result);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), entity);
        }
    }
    //endregion

    //region DeletePhisicalEntityById
    @Override
    public void delete_Crud_Entity_phisical_ById(String typeBean, Long id) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        repositoryPort.delete_Crud_Entity_phisical_ById(id);
    }

    @Override
    public void delete_Crud_Entity_phisical_JDBC_SP_ById(String typeBean, Long id) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        repositoryPort.delete_Crud_Entity_phisical_JDBC_SP_ById(id);
    }
    @Override
    public void delete_Crud_Entity_phisical_JPA_SP_ById(String typeBean, Long id) {
        Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
        repositoryPort.delete_Crud_Entity_phisical_JPA_SP_ById(id);
    }
    //endregion

    //region DeleteLogicalEntityById
    @Override
    public Object delete_Crud_Entity_logical_ById(String typeBean, Long id) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Crud_Entity entity = repositoryPort.delete_Crud_Entity_logical_ById(id);
            
            return helperEndpoints.buildResponse(1, "Eliminación lógica exitosa", null, null, entity);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), new Crud_Entity(id,null,null,null,null,null));
        }
    }

    @Override
    public Object delete_Crud_Entity_logical_JDBC_SP_ById(String typeBean, Long id) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Crud_Entity entity = repositoryPort.delete_Crud_Entity_logical_JDBC_SP_ById(id);
            
            return helperEndpoints.buildResponse(1, "Eliminación lógica exitosa", null, null, entity);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(), id);
        }
    }

    @Override
    public Object delete_Crud_Entity_logical_JPA_SP_ById(String typeBean, Long id) {
        try{
            Crud_RepositoryPort repositoryPort = getRepositoryPort(typeBean);
            Crud_Entity entity = repositoryPort.delete_Crud_Entity_logical_JPA_SP_ById(id);
            
            return helperEndpoints.buildResponse(1, "Eliminación lógica exitosa", null, null, entity);
        }catch(Exception e){
            return helperEndpoints.buildResponse(-1, e.getMessage(),  new Crud_Entity(id,null,null,null,null,null));
        }
    }
    //endregion
}