package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

public class Groupe {
    private String nom, description, auteur;
    private List<String> membres;
    private GestionBillets billets;

    public Groupe() {
        this.membres = new ArrayList<>();
        this.billets = new GestionBillets();
    }

    public Groupe(String nom, String description, String auteur) {
        this();
        this.nom = nom;
        this.description = description;
        this.auteur = auteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom != null) {
            this.nom = nom;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        if (auteur != null) {
            this.auteur = auteur;
        }
    }

    public List<String> getMembres() {
        return membres;
    }

    public void setMembres(String[] newMembres) {
        if (newMembres == null || newMembres.length == 0) {
            return;
        }                
        membres = new ArrayList<String>();
        for (String membre : newMembres) {
            membres.add(membre);
        }
    }

    public GestionBillets getBillets() {
        return billets;
    }
}
