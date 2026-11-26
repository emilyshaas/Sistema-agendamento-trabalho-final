
package ifrs.ibiruba.agendamentoservico.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("AgendamentoPU");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void fechar() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
