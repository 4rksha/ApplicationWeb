/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.mapping;
import fr.univlyon1.m1if.m1if03.classes.Billet;

/**
 *
 * @author vasli
 */
public class BilletMapper {
    public Integer id;
    public String titre, contenu, auteur;
    public String comments;
    
    public BilletMapper(Billet billet, Integer index, String groupe){
        this.id = index;
        this.titre = billet.getTitre();
        this.contenu = billet.getContenu();
        this.auteur = billet.getAuteur();
        this.comments = "/groupes/"+ groupe +"/billets/" + index + "/comments";
    }
}
