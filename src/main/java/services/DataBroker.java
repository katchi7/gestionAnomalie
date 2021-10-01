package services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Data
public class DataBroker {

    private EntityManager em;
    private static DataBroker dataBroker;
    private SessionFactory session;

    private DataBroker() {
        init();
    }

    private void init() {
        this.em = Persistence.createEntityManagerFactory("default").createEntityManager();
        this.session = em.unwrap(Session.class).getSessionFactory();

    }
    public static DataBroker getInstance(){
        if(dataBroker==null) dataBroker = new DataBroker();
        return dataBroker;
    }



}