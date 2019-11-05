package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

public class Groupe {
    private String nom;
    private String description;
    private String auteur;
    private List<String> participants;
    private GestionBillets gBillets;
    
    public Groupe() {
        nom = "Groupe";
        description = "";
        auteur = "anonyme";
        participants = new ArrayList<String>();
        gBillets = new GestionBillets();
    }

    public Groupe(String nom, String description, String auteur, List<String> participants, GestionBillets gBillets) {
        this.nom = nom;
        this.description = description;
        this.auteur = auteur;
        this.participants = participants;
        this.gBillets = gBillets;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public void setgBillets(GestionBillets gBillets) {
        this.gBillets = gBillets;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getAuteur() {
        return auteur;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public GestionBillets getgBillets() {
        return gBillets;
    }
    
}
