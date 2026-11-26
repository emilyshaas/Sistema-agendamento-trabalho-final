
package ifrs.ibiruba.agendamentoservico.repository;

import ifrs.ibiruba.agendamentoservico.model.Servico;
import ifrs.ibiruba.agendamentoservico.persistencia.JpaUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ServicoRepository {

    // Cadastrar serviço
    public void salvar(Servico servico) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(servico);

            em.getTransaction().commit();

        } catch (RuntimeException e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // Listar todos os serviços
    public List<Servico> listarTodos() {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT s FROM Servico s ORDER BY s.nome",
                    Servico.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Buscar serviço pelo ID
    public Servico buscarPorId(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.find(Servico.class, id);

        } finally {
            em.close();
        }
    }

    // Atualizar serviço
    public void atualizar(Servico servico) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(servico);

            em.getTransaction().commit();

        } catch (RuntimeException e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // Excluir serviço
    public void excluir(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Servico servico = em.find(Servico.class, id);

            if (servico != null) {
                em.remove(servico);
            }

            em.getTransaction().commit();

        } catch (RuntimeException e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }
}
