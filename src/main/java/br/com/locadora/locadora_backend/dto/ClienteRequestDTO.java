package br.com.locadora.locadora_backend.dto;

import java.time.LocalDate;
// IMPORTE AS VALIDAÇÕES
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(

        // ADICIONE A VALIDAÇÃO
        @NotBlank(message = "O nome completo é obrigatório")
        String nomeCompleto,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        String rg,

        @NotBlank(message = "A CNH é obrigatória")
        String cnh,

        @NotNull(message = "A validade da CNH é obrigatória")
        LocalDate validadeCNH,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        String telefoneExtra,
        String endereco,
        String cep,
        boolean temGaragem,
        LocalDate nascimento
) {}