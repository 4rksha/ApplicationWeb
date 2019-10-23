package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

public class Billet {
    private String titre, contenu, auteur;
    private List<Commentaire> commentaires;

    public Billet() {
        this.titre = "Rien";
        this.contenu = "Vide";
        this.auteur = "Personne";
        this.commentaires = new ArrayList<>();
    }

    public Billet(String titre, String contenu, String auteur) {
        this.titre = titre;
        this.contenu = contenu;
        this.auteur = auteur;
        this.commentaires = new ArrayList<>();
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
    
    public List<Commentaire> getCommentaires() {
        return commentaires;
    }
    
    public void addCommentaire(String auteur,String com) {
        commentaires.add(new Commentaire(auteur, com));
    }
}
