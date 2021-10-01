package services;

import jakarta.transaction.Transactional;
import model.Fiche;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FicheService {
    private static FicheService ficheService;
    private DataBroker dataBroker;

    private FicheService(){
        dataBroker = DataBroker.getInstance();
    }

    public static FicheService getInstance() {
        if(ficheService == null) ficheService = new FicheService();
        return ficheService  ;
    }
    public void save(Fiche fiche){
        Session session = dataBroker.getSession().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(fiche.getAction());
        session.save(fiche.getEmeteur());
        session.save(fiche.getAnomalie());
        session.save(fiche);
        transaction.commit();
        session.close();
    }
}
