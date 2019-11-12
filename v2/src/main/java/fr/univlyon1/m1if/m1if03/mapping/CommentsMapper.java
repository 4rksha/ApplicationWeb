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
public class CommentsMapper {
    
    public String[] commentaires;

    public CommentsMapper(String billetId, Billet billet, String groupe) {
        this.commentaires = new String[billet.getCommentaires().size()];
        int i = 0;
        for (String c : commentaires) {
            this.commentaires[i] = "/groupes/" + groupe + "/billets/" + billetId + "/commentaires/" + i;
            i++;
        }
    }   
    
    public String[] mapped() {
        return commentaires;
    }
}
