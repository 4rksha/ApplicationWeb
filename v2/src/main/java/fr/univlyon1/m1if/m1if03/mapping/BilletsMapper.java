package fr.univlyon1.m1if.m1if03.mapping;

import fr.univlyon1.m1if.m1if03.classes.GestionBillets;

/**
 *
 * @author vasli
 */
public class BilletsMapper {

    public String[] billets;
    
    public BilletsMapper (GestionBillets gBillet, String groupe) {
        int size = gBillet.getNbBillets();
        billets = new String[size];
        for (int i = 0; i < size; ++i) {
            this.billets[i] = "/groupes/"+ groupe +"/billets/" + i;
        }
    }
    
}
