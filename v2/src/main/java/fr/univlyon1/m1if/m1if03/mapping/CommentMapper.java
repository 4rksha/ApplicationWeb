/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.mapping;

import fr.univlyon1.m1if.m1if03.classes.Commentaire;

/**
 *
 * @author zac
 */
public class CommentMapper {
    public String id;
    public String auteur;
    public String texte;
    
    public CommentMapper(String id, Commentaire c) {
        this.id = id;
        this.auteur = c.getAuteur();
        this.texte = c.getText();
    }
}
