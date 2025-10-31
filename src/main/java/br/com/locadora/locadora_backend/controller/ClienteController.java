package br.com.locadora.locadora_backend.controller;

import jakarta.validation.Valid;
import br.com.locadora.locadora_backend.dto.ClienteRequestDTO;
import br.com.locadora.locadora_backend.dto.ClienteResponseDTO;
import br.com.locadora.locadora_backend.dto.LoginRequestDTO;
import br.com.locadora.locadora_backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.locadora.locadora_backend.dto.AlterarSenhaRequestDTO;
import br.com.locadora.locadora_backend.dto.ClienteUpdateDTO;
import br.com.locadora.locadora_backend.dto.AlterarSenhaRequestDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
// @CrossOrigin(origins = "http://localhost:4200") // Descomente quando for usar o Angular
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @GetMapping
    public List<ClienteResponseDTO> listarClientes() {
        return clienteService.listarTodos();
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        try {
            ClienteResponseDTO clienteLogado = clienteService.login(loginDTO);
            return ResponseEntity.ok(clienteLogado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarCliente(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        try {
            ClienteResponseDTO novoCliente = clienteService.criarCliente(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteUpdateDTO requestDTO) {

        try {
            ClienteResponseDTO clienteAtualizado = clienteService.atualizarCliente(id, requestDTO);
            return ResponseEntity.ok(clienteAtualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<?> alterarSenha(
            @PathVariable Integer id,
            @Valid @RequestBody AlterarSenhaRequestDTO requestDTO) {

        try {
            clienteService.alterarSenha(id, requestDTO);
            return ResponseEntity.ok("Senha alterada com sucesso.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}