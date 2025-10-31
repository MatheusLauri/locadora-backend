package br.com.locadora.locadora_backend.dto;


public record ClienteResponseDTO(
        Integer id,
        String nomeCompleto,
        String email,
        String telefone,
        String cnh
) {}