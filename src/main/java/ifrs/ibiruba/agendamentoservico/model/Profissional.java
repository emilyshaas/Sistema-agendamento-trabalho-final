
package ifrs.ibiruba.agendamentoservico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profissionais")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 80)
    private String especialidade;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false)
    private boolean ativo;

    public Profissional() {
    }

    public Profissional(String nome, String especialidade,
            String telefone, boolean ativo) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return nome;
    }
}
