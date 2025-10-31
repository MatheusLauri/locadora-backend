package br.com.locadora.locadora_backend.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_Cliente")
    private Integer id;

    @Column(name = "Ds_Nome", nullable = false)
    private String nomeCompleto;

    @Column(name = "DS_CPF", nullable = false, unique = true)
    private String cpf;

    @Column(name = "DS_RG", unique = true)
    private String rg;

    @Column(name = "DS_CNH", nullable = false, unique = true)
    private String cnh;

    @Column(name = "DT_ValidadeCNH", nullable = false)
    private LocalDate validadeCNH;

    @Column(name = "DS_Email", nullable = false, unique = true)
    private String email;

    @Column(name = "DS_Telefone", nullable = false)
    private String telefone;

    @Column(name = "DS_TelefoneExtra")
    private String telefoneExtra;

    @Column(name = "DS_Endereco")
    private String endereco;

    @Column(name = "DS_CEP")
    private String cep;

    @Column(name = "BL_TemGaragem")
    private boolean temGaragem;

    @Column(name = "DT_Nascimento")
    private LocalDate nascimento;

    @Column(name = "DS_Senha", nullable = false)
    private String senha;
}