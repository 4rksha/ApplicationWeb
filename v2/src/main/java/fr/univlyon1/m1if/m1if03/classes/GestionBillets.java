package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestion de billets
 */
public class GestionBillets {

    private final List<Billet> billets;
    
    public GestionBillets() {
        billets = new ArrayList<Billet>();
    }


    public void add( Billet billet) {
        billets.add(billet);
    }
    
    public Billet getBillet( int i) {
        if (i >= 0  && i < billets.size()){
            return billets.get(i);
        } 
        return null;
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
    
    public void addCommantaireBillet( String author, int id, String text) {
        Billet billet = this.getBillet(id);
        if (billet != null) {
            billet.addCommentaire(author, text);
        }
    }
    
    public int getNbBillets() {
        return billets.size();
    }

    public Billet getLastBillet() {
        if (billets.size()> 0) {
            return this.getBillet( billets.size() - 1);
        }
        throw new IndexOutOfBoundsException("Erreur dans l'appel à la fonction getLastBillet");
    }
    public void deleteBillet(int id) {
        if (id >= 0 && id < billets.size()){
            billets.remove(id);
        }
    }
    
}
