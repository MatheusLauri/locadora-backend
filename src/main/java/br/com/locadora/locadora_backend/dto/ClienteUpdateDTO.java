package br.com.locadora.locadora_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;


public record ClienteUpdateDTO(

        @NotBlank(message = "O nome completo é obrigatório")
        String nomeCompleto,

        @NotBlank(message = "O CPF é obrigatório")
        @Size(min = 14, max = 14, message = "O CPF deve ter 14 caracteres (XXX.XXX.XXX-XX)")
        String cpf,

        String rg,

        @NotBlank(message = "A CNH é obrigatória")
        @Size(min = 11, max = 11, message = "A CNH deve ter 11 dígitos")
        String cnh,

        @NotNull(message = "A validade da CNH é obrigatória")
        LocalDate validadeCNH,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        String telefoneExtra,
        String endereco,
        String cep,
        boolean temGaragem,
        LocalDate nascimento
) {}