package com.dcknotfnd.Ms_mascota.repository;

import com.dcknotfnd.Ms_mascota.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    
    @Query("SELECT m FROM Mascota m WHERE " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:type IS NULL OR m.type = :type) AND " +
           "(:query IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Mascota> buscarConFiltros(@Param("status") String status, 
                                   @Param("type") String type, 
                                   @Param("query") String query);
}