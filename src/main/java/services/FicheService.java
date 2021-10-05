package services;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import model.Anomalie;
import model.Fiche;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class FicheService {
    private static FicheService ficheService;
    private DataBroker dataBroker;
    private FicheService(){
        dataBroker = DataBroker.getInstance();
    }
    private static final String  FICH_PATH="../Assets/Fiche.pdf";
    public static FicheService getInstance() {
        if(ficheService == null) ficheService = new FicheService();
        return ficheService  ;
    }
    public void save(Fiche fiche){
        Session session = dataBroker.getSession().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(fiche.getAction());
        session.saveOrUpdate(fiche.getEmeteur());
        session.saveOrUpdate(fiche.getAnomalie());
        session.saveOrUpdate(fiche);
        transaction.commit();
        session.close();
    }
    public List<Fiche> getAllFiches(){
        Session session = dataBroker.getSession().openSession();
        List<Fiche> fiches =  session.createQuery("SELECT f  FROM Fiche as f ",Fiche.class).getResultList();
        session.close();
        return fiches;
    }
    @Transactional
    public void  deleteFiche(Fiche fiche){
        Session session =  dataBroker.getSession().openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(session.contains(fiche.getAnomalie())?fiche.getAnomalie():session.merge(fiche.getAnomalie()));
        session.remove(session.contains(fiche.getEmeteur())?fiche.getEmeteur():session.merge(fiche.getEmeteur()));
        session.remove(session.contains(fiche.getAction())?fiche.getAction():session.merge(fiche.getAction()));
        session.remove(session.contains(fiche)?fiche:session.merge(fiche));
        transaction.commit();
        session.close();
    }
    public void imprimer(String path,Fiche fiche) throws IOException {

        PDDocument pdDocument = PDDocument.load(getClass().getResourceAsStream(FICH_PATH));
        PDAcroForm form = pdDocument.getDocumentCatalog().getAcroForm();
        System.out.println(form);
        PDField field = form.getField("Emetteur");
        field.setValue(fiche.getEmeteur().getFullName());
        field = form.getField("Email");
        field.setValue(fiche.getEmeteur().getEmail());
        field = form.getField("Date");
        field.setValue(fiche.getDateFormatted());
        field = form.getField("nFiche");
        field.setValue(fiche.getId()+"");
        field = form.getField("Process");
        field.setValue(fiche.getProcess());
        field = form.getField("NCRemarque");
        field.setValue(fiche.getRemarqueStatut());
        field = form.getField("RemarqueAction");
        field.setValue(fiche.getAction().getRemarque());
        PDCheckBox checkBox = null;
        if("interne".equals(fiche.getEmeteur().getUserType())){
            checkBox = (PDCheckBox) form.getField("Int");
            checkBox.check();
        }
        List<Anomalie> freqAnomalies = getFreqAnomalie();
        int i = 1;
        for (Anomalie freqAnomaly : freqAnomalies) {

            field = form.getField("item"+i);
            field.setValue(freqAnomaly.getDesc());
            if(i==8) break;
            i++;
        }
        if ("externe".equals(fiche.getEmeteur().getUserType())){
            checkBox = (PDCheckBox) form.getField("Ext");
            checkBox.check();
        }
        if(fiche.getAnomalie().getFreq()!=null && fiche.getAnomalie().getFreq()){
            //TODO: load frequente anomalies
            boolean checked = false;
            for (i =1;i<=8;i++){
                field = form.getField("item"+i);
                if(field.getValueAsString().equals(fiche.getAnomalie().getDesc())){
                    checkBox = (PDCheckBox) form.getField("item"+i+"RB");
                    checkBox.check();
                    checked = true;
                    break;
                }
            }
            if(!checked){
                field = form.getField("item8");
                field.setValue(fiche.getAnomalie().getDesc());
                checkBox = (PDCheckBox) form.getField("item8RB");
                checkBox.check();
            }
        }
        else {
            checkBox = (PDCheckBox) form.getField("autre");
            checkBox.check();
            field = form.getField("autreText");
            System.out.println(fiche.getAnomalie().getDesc());
            field.setValue(fiche.getAnomalie().getDesc());
        }
        if(fiche.getNonConfirmite()){
            checkBox = (PDCheckBox) form.getField("NCTrue");
            checkBox.check();
        }
        else {
            checkBox = (PDCheckBox) form.getField("NCFalse");
            checkBox.check();
        }
        if("Rebut".equals(fiche.getAction().getName())){
            checkBox = (PDCheckBox) form.getField("Rebut");
            checkBox.check();
        }

        if("Correction".equals(fiche.getAction().getName())){
            checkBox = (PDCheckBox) form.getField("Correction");
            checkBox.check();

        }

        if("Derogation".equals(fiche.getAction().getName())){
            checkBox = (PDCheckBox) form.getField("Derogation");
            checkBox.check();

        }
        for (PDField formField : form.getFields()) {
            formField.setReadOnly(true);
        }
        removeEmpty(pdDocument);
        pdDocument.save(new File(path+"\\Fiche"+fiche.getId()+".pdf"));
    }


    public List<Anomalie> getFreqAnomalie(){
        Session session = dataBroker.getSession().openSession();
        List<Anomalie> anomalies = session.createQuery("SELECT a FROM Anomalie as a where a.freq=true ",Anomalie.class).getResultList();
        session.close();
        return anomalies;
    }
    @SneakyThrows
    public void removeEmpty(PDDocument document){
        PDField field = null;
        PDAcroForm form = document.getDocumentCatalog().getAcroForm();
        PDCheckBox checkBox = null;
        for (int i = 1; i < 9; i++) {
            field = form.getField("item"+i);
            System.out.println(field);
            if(field.getValueAsString().trim().isEmpty()){
                System.out.println(("item" + i + "RB"));
                removeField(document,field.getFullyQualifiedName());
                removeField(document,"item"+i+"RB");
            }
        }
    }
    public void saveAllAnomalies(List<Anomalie> anomalies){
        Session session = dataBroker.getSession().openSession();
        Transaction transaction = session.beginTransaction();
        for (Anomalie anomaly : anomalies) {
            session.save(anomaly);
        }
        transaction.commit();
        session.close();
    }
    public static void removeField(PDDocument doc, String subject) {
        COSObject kid = (COSObject)((COSArray)doc.getDocumentCatalog().getPages().getCOSObject().getDictionaryObject(COSName.KIDS)).get(0);
        COSArray annotations = (COSArray)kid.getDictionaryObject(COSName.ANNOTS);
        COSObject removeAnnotation = null;
        for(COSBase annotation : annotations) {
            if(annotation!=null) {
                COSObject annot = (COSObject)annotation;
                if(annot.getItem(COSName.SUBJ)!=null) {
                    //System.out.println(((COSString)annot.getItem(COSName.SUBJ)).toString());
                    if(((COSString)annot.getItem(COSName.SUBJ)).getString().equals(subject)) {
                        removeAnnotation=annot;
                    }
                }
            }
        }
        if(removeAnnotation!=null) {
            annotations.removeObject(removeAnnotation);
        }
    }
}
