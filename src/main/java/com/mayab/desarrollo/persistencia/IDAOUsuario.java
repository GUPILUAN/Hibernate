package com.mayab.desarrollo.persistencia;
import java.util.List;

import com.mayab.desarrollo.entities.Usuario;

public interface IDAOUsuario {
    public int insertarUsuario(Usuario usuario);
    public Usuario consultarUsuario(int  idUsuario);
    public boolean actualizaUsuario(int id, String nombreNuevo, String  contrasenaNueva, String emailNuevo);
    public boolean borrarUsuario(int id);
    public List<Usuario> listarTodosLosUsuarios();
    public Usuario validarUsuario(String usuario, String email);
    public int obtenerHueco();
}
