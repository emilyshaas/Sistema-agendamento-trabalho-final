
package ifrs.ibiruba.agendamentoservico.repository;

import ifrs.ibiruba.agendamentoservico.model.Profissional;
import ifrs.ibiruba.agendamentoservico.persistencia.JpaUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ProfissionalRepository {

    // Cadastrar profissional
    public void salvar(Profissional profissional) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(profissional);

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

    // Listar todos os profissionais
    public List<Profissional> listarTodos() {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Profissional p ORDER BY p.nome",
                    Profissional.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Buscar profissional pelo ID
    public Profissional buscarPorId(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.find(Profissional.class, id);

        } finally {
            em.close();
        }
    }

    // Atualizar profissional
    public void atualizar(Profissional profissional) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(profissional);

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

    // Excluir profissional
    public void excluir(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Profissional profissional =
                    em.find(Profissional.class, id);

            if (profissional != null) {
                em.remove(profissional);
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
