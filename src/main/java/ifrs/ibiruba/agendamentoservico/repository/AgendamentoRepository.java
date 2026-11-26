
package ifrs.ibiruba.agendamentoservico.repository;

import ifrs.ibiruba.agendamentoservico.model.Agendamento;
import ifrs.ibiruba.agendamentoservico.persistencia.JpaUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class AgendamentoRepository {

    // Cadastrar agendamento
    public void salvar(Agendamento agendamento) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(agendamento);

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

    // Listar todos os agendamentos
    public List<Agendamento> listarTodos() {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT a FROM Agendamento a "
                    + "JOIN FETCH a.cliente "
                    + "JOIN FETCH a.profissional "
                    + "JOIN FETCH a.servico "
                    + "ORDER BY a.data, a.horario",
                    Agendamento.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Buscar agendamento pelo ID
    public Agendamento buscarPorId(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT a FROM Agendamento a "
                    + "JOIN FETCH a.cliente "
                    + "JOIN FETCH a.profissional "
                    + "JOIN FETCH a.servico "
                    + "WHERE a.id = :id",
                    Agendamento.class
            )
            .setParameter("id", id)
            .getSingleResult();

        } finally {
            em.close();
        }
    }

    // Atualizar agendamento
    public void atualizar(Agendamento agendamento) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(agendamento);

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

    // Excluir agendamento
    public void excluir(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Agendamento agendamento =
                    em.find(Agendamento.class, id);

            if (agendamento != null) {
                em.remove(agendamento);
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
