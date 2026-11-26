
module ifrs.ibiruba.agendamentoservico {

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Banco de dados e persistência
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.hibernate.orm.community.dialects;
    requires java.base;

    // Acesso do JavaFX aos controllers
    opens ifrs.ibiruba.agendamentoservico to javafx.fxml;
    opens ifrs.ibiruba.agendamentoservico.controller to javafx.fxml;

    // Acesso do Hibernate às entidades
    opens ifrs.ibiruba.agendamentoservico.model
        to org.hibernate.orm.core, javafx.base;
    
    opens ifrs.ibiruba.agendamentoservico.persistencia
        to org.hibernate.orm.core;
    
    opens ifrs.ibiruba.agendamentoservico.repository
        to org.hibernate.orm.core;

    exports ifrs.ibiruba.agendamentoservico;
}
