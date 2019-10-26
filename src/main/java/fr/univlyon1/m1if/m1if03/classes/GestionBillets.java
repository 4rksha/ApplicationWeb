package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class GestionBillets {

    /** static reference du singloton */
    private static GestionBillets singleton;
    private final Map<String, List<Billet>> groupes;
    
    private GestionBillets() {
        this.groupes = new HashMap<>();
    }

    public static GestionBillets getInstance() {
        if (singleton == null) {
            singleton = new GestionBillets();
        }
        return singleton;
    }

    public void add(String groupe, Billet billet) {
        if (!groupes.containsKey(groupe)) {
            addGroup(groupe);
        }
        groupes.get(groupe).add(billet);
    }
    
    public void addGroup(String groupe) {
        if (!groupes.containsKey(groupe)) {
            groupes.put(groupe, new ArrayList<Billet>());
        }
    }

    public Billet getBillet(String groupe, int i) {
        return groupes.get(groupe).get(i);
    }
    
    /**
     * 
     * @param groupe
     * @param title
     * @return un billet si il trouve le billet sinon null
     */
    public Billet findBillet(String groupe, String title) {
        List<Billet> billets = groupes.get(groupe);
        for (int i = 0; i < billets.size(); ++i) {
            if (billets.get(i).getTitre().equals(title)) {
                return billets.get(i);
            }
        }
        return null;
    }
    
    public void addCommantaireBillet(String groupe, String author, int id, String text) {
        Billet billet = this.getBillet(groupe, id);
        if (billet != null) {
            billet.addCommentaire(author, text);
        }
    }
    
    public int getNbBillets(String groupe) {
        if (groupes.get(groupe) == null) {
            return 0;
        }
        return groupes.get(groupe).size();
    }

    public Billet getLastBillet(String groupe) {
        if (getNbBillets(groupe)> 0) {
            return this.getBillet(groupe, getNbBillets(groupe) - 1);
        }
        throw new IndexOutOfBoundsException("Erreur dans l'appel à la fonction getLastBillet");
    }
    
    public Set<String> getGroupes() {
        return groupes.keySet();
    }
}
