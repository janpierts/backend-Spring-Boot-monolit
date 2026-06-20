package com.rj.esqueleto.admin.crud.infrastructure.persistence.springdata;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rj.esqueleto.admin.crud.infrastructure.persistence.entity.CrudEntityJpa;

//@Repository
public interface crudSpringDataRepository extends JpaRepository<CrudEntityJpa, Long> {
    Optional<CrudEntityJpa> findByName(String name);
    List<CrudEntityJpa> findByNameIn(Collection<String> names);
}