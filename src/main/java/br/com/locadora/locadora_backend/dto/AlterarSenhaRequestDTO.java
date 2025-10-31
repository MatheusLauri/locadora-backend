package br.com.locadora.locadora_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaRequestDTO(
        @NotBlank(message = "A senha antiga é obrigatória")
        String senhaAntiga,

        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
        String novaSenha
) {}