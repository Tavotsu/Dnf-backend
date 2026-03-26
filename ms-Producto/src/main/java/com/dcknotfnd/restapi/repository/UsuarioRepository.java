package com.dcknotfnd.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dcknotfnd.restapi.model.Usuario;
@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // JPA crea la consulta SQL automáticamente solo con nombrar bien el método
    Usuario findByEmail(String email);
}
