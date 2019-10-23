package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GestionBillets {

    /** static reference du singloton */
    private static GestionBillets singleton;
    private List<Billet> billets;
    
    private GestionBillets() {
        this.billets = new ArrayList<>();
    }

    public static GestionBillets getInstance() {
        if (singleton == null) {
            singleton = new GestionBillets();
        }
        return singleton;
    }

    public void add(Billet billet) {
        this.billets.add(billet);
    }

    public Billet getBillet(int i) {
        return billets.get(i);
    }
    
    /**
     * 
     * @param title
     * @return un billet si il trouve le billet sinon null
     */
    public Billet findBillet(String title) {
        for (int i = 0; i < billets.size(); ++i) {
            if (billets.get(i).getTitre().equals(title)) {
                return billets.get(i);
            }
        }
        return null;
    }
    
    public void addCommantaireBillet(String author, String title, String text) {
        Billet billet = this.findBillet(title);
        if (billet != null) {
//            Integer index = billets.indexOf(billet);
            billet.addCommentaire(author, text);
//            billets.set(index, billet);
        }
    }
    
    public int getNbBillets() {
        return billets.size();
    }

    public Billet getLastBillet() {
        if (billets.size() > 0)
            return this.getBillet(billets.size() -1);
        throw new IndexOutOfBoundsException("Erreur dans l'appel à la fonction getLastBillet");
    }
}
