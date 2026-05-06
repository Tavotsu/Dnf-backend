package com.usuario.Ms_usuario.dto;

import com.usuario.Ms_usuario.model.Usuario; // O usa un UsuarioDTO si prefieres ocultar datos

public record AuthResponse(String token, Usuario user) {
}