package fr.univlyon1.m1if.m1if03.mapping;

import fr.univlyon1.m1if.m1if03.classes.*;

/**
 *
 * @author vasli
 */
public class GroupeMapper {

    public String nom, description, proprietaire;
    public String[] menbres;
    public String billets;

    public GroupeMapper(Groupe groupe) {
        this.nom = groupe.getNom();
        this.description = groupe.getDescription();
        this.proprietaire = groupe.getAuteur();
        billets = "/groupes/" + groupe.getNom() + "/billets";
        int sizeMenbers = groupe.getMembres().size();
        menbres = new String[sizeMenbers];
        for (int i = 0; i < sizeMenbers; ++i ) {
            menbres[i] = groupe.getMembres().get(i);
        }

    }
}
