package com.usuario.Ms_usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usuario.Ms_usuario.model.Usuario;
@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // JPA crea la consulta SQL automáticamente solo con nombrar bien el método
    Usuario findByEmail(String email);
}
