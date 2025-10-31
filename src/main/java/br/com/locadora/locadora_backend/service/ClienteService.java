package br.com.locadora.locadora_backend.service;

import br.com.locadora.locadora_backend.dto.ClienteRequestDTO;
import br.com.locadora.locadora_backend.dto.ClienteResponseDTO;
import br.com.locadora.locadora_backend.dto.LoginRequestDTO;
import br.com.locadora.locadora_backend.model.Cliente;
import br.com.locadora.locadora_backend.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.locadora.locadora_backend.dto.AlterarSenhaRequestDTO;
import br.com.locadora.locadora_backend.dto.ClienteUpdateDTO;
import br.com.locadora.locadora_backend.dto.ClienteRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<ClienteResponseDTO> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public void alterarSenha(Integer id, AlterarSenhaRequestDTO requestDTO) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        if (!passwordEncoder.matches(requestDTO.senhaAntiga(), cliente.getSenha())) {
            throw new IllegalArgumentException("A senha anterior está incorreta.");
        }

        cliente.setSenha(passwordEncoder.encode(requestDTO.novaSenha()));
        clienteRepository.save(cliente);

    }
    public ClienteResponseDTO criarCliente(ClienteRequestDTO requestDTO) {

        // Validação de duplicidade (como você fez, agora incluindo Email)
        if (clienteRepository.existsByCpf(requestDTO.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema.");
        }
        if (clienteRepository.existsByCnh(requestDTO.cnh())) {
            throw new IllegalArgumentException("CNH já cadastrada no sistema.");
        }
        if (clienteRepository.existsByEmail(requestDTO.email())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema.");
        }
        if (requestDTO.rg() != null && !requestDTO.rg().isBlank()) {
            if (clienteRepository.existsByRg(requestDTO.rg())) {
                throw new IllegalArgumentException("RG já cadastrado no sistema.");
            }
        }

        // 1. Converte DTO para Entidade
        Cliente novoCliente = this.converterParaEntidade(requestDTO);

        // 2. Criptografa a senha
        novoCliente.setSenha(passwordEncoder.encode(requestDTO.senha()));

        // 3. Salva no banco
        Cliente clienteSalvo = clienteRepository.save(novoCliente);

        // 4. Retorna o DTO de Resposta
        return this.converterParaResponseDTO(clienteSalvo);
    }

    /**
     * PUT (Atualizar Cliente)
     */
    public ClienteResponseDTO atualizarCliente(Integer id, ClienteUpdateDTO updateDTO) { // <--- MUDANÇA AQUI

        // 1. Verifica se o cliente do ID existe
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + id));

        // 2. Validação de duplicidade (passando os campos do novo DTO)
        validarDuplicidadeAoAtualizar(id, updateDTO.cpf(), updateDTO.cnh(), updateDTO.rg(), updateDTO.email());

        // 3. Atualiza os dados do objeto 'clienteExistente'
        clienteExistente.setNomeCompleto(updateDTO.nomeCompleto());
        clienteExistente.setCpf(updateDTO.cpf());
        clienteExistente.setRg(updateDTO.rg());
        clienteExistente.setCnh(updateDTO.cnh());
        clienteExistente.setValidadeCNH(updateDTO.validadeCNH());
        clienteExistente.setEmail(updateDTO.email());
        clienteExistente.setTelefone(updateDTO.telefone());
        clienteExistente.setTelefoneExtra(updateDTO.telefoneExtra());
        clienteExistente.setEndereco(updateDTO.endereco());
        clienteExistente.setCep(updateDTO.cep());
        clienteExistente.setTemGaragem(updateDTO.temGaragem());
        clienteExistente.setNascimento(updateDTO.nascimento());

        // 5. Salva
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

        // 6. Retorna o DTO
        return this.converterParaResponseDTO(clienteAtualizado);
    }


    public ClienteResponseDTO login(LoginRequestDTO loginDTO) {

        // 1. Busca o cliente SOMENTE pelo email
        Cliente cliente = clienteRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos."));

        // 2. Compara a senha enviada (texto puro) com a senha do banco (hash)
        if (passwordEncoder.matches(loginDTO.senha(), cliente.getSenha())) {
            // Se baterem, retorna o DTO de Resposta (Login sucesso)
            return this.converterParaResponseDTO(cliente);
        } else {
            // Se não baterem, lança a exceção
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }
    }



    private void validarDuplicidadeAoAtualizar(Integer id, String cpf, String cnh, String rg, String email) {
        Optional<Cliente> clienteComCpf = clienteRepository.findByCpf(cpf);
        if (clienteComCpf.isPresent() && !clienteComCpf.get().getId().equals(id)) {
            throw new IllegalArgumentException("Este CPF já pertence a outro cliente.");
        }

        Optional<Cliente> clienteComCnh = clienteRepository.findByCnh(cnh);
        if (clienteComCnh.isPresent() && !clienteComCnh.get().getId().equals(id)) {
            throw new IllegalArgumentException("Esta CNH já pertence a outro cliente.");
        }

        // ADICIONAR ESTA VERIFICAÇÃO DE EMAIL
        Optional<Cliente> clienteComEmail = clienteRepository.findByEmail(email);
        if (clienteComEmail.isPresent() && !clienteComEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Este Email já pertence a outro cliente.");
        }

        if (rg != null && !rg.isBlank()) {
            Optional<Cliente> clienteComRg = clienteRepository.findByRg(rg);
            if (clienteComRg.isPresent() && !clienteComRg.get().getId().equals(id)) {
                throw new IllegalArgumentException("Este RG já pertence a outro cliente.");
            }
        }
    }

    private Cliente converterParaEntidade(ClienteRequestDTO requestDTO) {
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto(requestDTO.nomeCompleto());
        cliente.setCpf(requestDTO.cpf());
        cliente.setRg(requestDTO.rg());
        cliente.setCnh(requestDTO.cnh());
        cliente.setValidadeCNH(requestDTO.validadeCNH());
        cliente.setEmail(requestDTO.email());
        cliente.setTelefone(requestDTO.telefone());
        cliente.setTelefoneExtra(requestDTO.telefoneExtra());
        cliente.setEndereco(requestDTO.endereco());
        cliente.setCep(requestDTO.cep());
        cliente.setTemGaragem(requestDTO.temGaragem());
        cliente.setNascimento(requestDTO.nascimento());
        return cliente;
    }

    private ClienteResponseDTO converterParaResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNomeCompleto(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCnh()
        );
    }
}