package org.rentalstore.entity;

import java.util.HashMap;
import java.util.Map;

public class Role {
    private static HashMap<Integer, String> roles = new HashMap<>();

    public Role() {
        roles.put(1, "admin");
        roles.put(2, "customer");
        roles.put(3, "manager");
        roles.put(4, "employee");
        roles.put(5, "user");

    }
    public HashMap<Integer, String> getRoles() {
        return roles;
    }
    public void setRoles(HashMap<Integer, String> roles) {
        this.roles = roles;
    }
    public static String getRolById(int id){
        return roles.get(id);
    }
    public static Integer getIdByRol(String rol) {
        // Búsqueda inversa en el HashMap
        for (Map.Entry<Integer, String> entry : roles.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(rol)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
