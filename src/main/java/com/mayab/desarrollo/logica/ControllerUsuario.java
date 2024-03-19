package com.mayab.desarrollo.logica;
import java.util.List;

import com.mayab.desarrollo.entities.Usuario;
import com.mayab.desarrollo.persistencia.IDAOUsuario;

public class ControllerUsuario {
    private IDAOUsuario dao;

    public ControllerUsuario(IDAOUsuario  dao) {
        this.dao = dao;
    }
    
    public int crearUsuario(String nombre, String contrasena, String email){
        if(dao.validarUsuario(nombre, email) == null){
            Usuario user = new Usuario();
            user.setNombre(nombre);
            user.setPassword(!contrasena.isBlank() ? contrasena : nombre.substring(0, 3) + email.substring(0, 3));
            user.setEmail(email);
            return dao.insertarUsuario(user);
        }
        System.out.println("Ya se encuentra el usuario en la base de datos");
        return -1;
    }

    public Usuario obtenerUsuario(int id){
        return dao.consultarUsuario(id);
    }

    public boolean actualizaUsuario(int id, String usuario, String contrasena, String email){
        Usuario user = dao.validarUsuario(usuario, email);
        if(user != null){
            id = user.getId();
        }
        return dao.actualizaUsuario(id, usuario, contrasena, email);
    }

    public boolean eliminarUsuario(int id){
         return dao.borrarUsuario(id);
    }

    public List<Usuario> obtenerUsuarios(){
        return dao.listarTodosLosUsuarios();
    }

    public void borrarMuchosUsuarios(String ids){
            String[] separeIds = ids.split(",");
            for (String string : separeIds) {
                try {
                    dao.borrarUsuario(Integer.parseInt(string.trim()));
                } catch (Exception e) {
                    System.out.println("No se pudo convertir a numero");
                }
            }
    }
}
