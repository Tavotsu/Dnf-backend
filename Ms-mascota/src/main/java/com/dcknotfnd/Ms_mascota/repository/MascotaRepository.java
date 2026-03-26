package com.dcknotfnd.Ms_mascota.repository;
import com.dcknotfnd.Ms_mascota.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByEspecieAndRaza(String especie, String raza);

    // Cambiamos findByUsuario a findByUsuarioId
    List<Mascota> findByUsuarioId(Long usuarioId);
}