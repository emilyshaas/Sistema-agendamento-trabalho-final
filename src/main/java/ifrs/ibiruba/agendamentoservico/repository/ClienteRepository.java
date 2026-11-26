
package ifrs.ibiruba.agendamentoservico.repository;

import ifrs.ibiruba.agendamentoservico.model.Cliente;
import ifrs.ibiruba.agendamentoservico.persistencia.JpaUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ClienteRepository {

    // Cadastrar cliente
    public void salvar(Cliente cliente) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(cliente);

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

    // Listar todos os clientes
    public List<Cliente> listarTodos() {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c ORDER BY c.nome",
                    Cliente.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Buscar cliente pelo ID
    public Cliente buscarPorId(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            return em.find(Cliente.class, id);

        } finally {
            em.close();
        }
    }

    // Atualizar cliente
    public void atualizar(Cliente cliente) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(cliente);

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

    // Excluir cliente
    public void excluir(Long id) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Cliente cliente = em.find(Cliente.class, id);

            if (cliente != null) {
                em.remove(cliente);
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
