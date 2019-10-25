package fr.univlyon1.m1if.m1if03.classes;

public class Commentaire {
    private String auteur;
    private String text;

    Commentaire(String auteur,String text) {
        this.auteur = auteur;
        this.text = text; 
    }
    
    public void setCommentaire(String auteur, String text) {
        this.auteur = auteur;
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.auteur + " : " + this.text;
    }    
}
