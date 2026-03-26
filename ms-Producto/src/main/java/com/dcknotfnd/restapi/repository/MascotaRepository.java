package com.dcknotfnd.restapi.repository;

import com.dcknotfnd.restapi.model.Mascota;
import com.dcknotfnd.restapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByEspecieAndRaza(String especie, String raza);
    List<Mascota> findByUsuario(Long usuarioId);
}
