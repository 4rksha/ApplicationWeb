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

//    public void outJSON(PrintWriter out) throws IOException {
//        JsonFactory factory = new JsonFactory();
//        JsonGenerator generator = factory.createGenerator(out);
//        generator.writeStartObject();
//        // Listage des groupes
//        generator.writeFieldName("groupes");
//        generator.writeStartArray();
//        for (DetailGroupe groupe : this.groupes) {
//            generator.writeStartObject(); 
//            generator.writeStringField("id", groupe.id);
//            generator.writeStringField("uri", groupe.uri);
//            generator.writeEndObject();
//        }
//        generator.writeEndArray();
//        generator.writeEndObject();
//        // Fin de la génération du code
//        generator.close();
//    }
}
