package fr.univlyon1.m1if.m1if03.classes;

public class Commentaire {
    private String auteur;
    private String text;

    public Commentaire() {
        this.auteur = "";
        this.text = "";
    }
    
    public Commentaire(String auteur,String text) {
        this.auteur = auteur;
        this.text = text; 
    }
    
    public void setCommentaire(String auteur, String text) {
        this.auteur = auteur;
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    public String getAuteur() {
        return auteur;
    }
    
    @Override
    public String toString() {
        return this.auteur + " : " + this.text;
    }    
}
