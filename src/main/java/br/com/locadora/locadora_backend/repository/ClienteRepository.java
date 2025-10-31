package br.com.locadora.locadora_backend.repository;

import br.com.locadora.locadora_backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    boolean existsByCpf(String cpf);
    boolean existsByCnh(String cnh);
    boolean existsByRg(String rg);
    boolean existsByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByCnh(String cnh);
    Optional<Cliente> findByRg(String rg);
    Optional<Cliente> findByEmail(String email);
}