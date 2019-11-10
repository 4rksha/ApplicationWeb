package fr.univlyon1.m1if.m1if03.classes;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Billet {
    private String titre, contenu, auteur;
    private List<Commentaire> commentaires;
    private long lastModifTime;

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
        this.lastModifTime = new Date().getTime();
    }

    public String getTitre() {
        return this.titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
        this.lastModifTime = new Date().getTime();
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
        this.lastModifTime = new Date().getTime();
    }

    public String getAuteur() {
        return this.auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
        this.lastModifTime = new Date().getTime();
    }
        
    public long getLastModifTime() {
        return this.lastModifTime;
    }
    
    public List<Commentaire> getCommentaires() {
        return this.commentaires;
    }
    
    public Commentaire getCommentaire(int index) {
        if (index < 0 || index >= commentaires.size()) {
            return null;
        }
        return commentaires.get(index);
    }
    
    public void addCommentaire(String auteur,String com) {
        this.commentaires.add(new Commentaire(auteur, com));
        this.lastModifTime = new Date().getTime();
    }
    
    public void removeCommentaire(int i) {
        this.commentaires.remove(i);
        this.lastModifTime = new Date().getTime();
    }
}
