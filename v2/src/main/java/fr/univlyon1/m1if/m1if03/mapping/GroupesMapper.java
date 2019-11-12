/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univlyon1.m1if.m1if03.mapping;

import java.util.Set;

/**
 *
 * @author vasli
 */
public class GroupesMapper {
    
    public String[] groupes;

    public GroupesMapper(Set<String> groupesNames) {
        this.groupes = new String[groupesNames.size()];
        int i = 0;
        for (String groupe : groupesNames) {
            this.groupes[i] =  "/groupes/" + groupe;
            i++;
        }
    }
}
