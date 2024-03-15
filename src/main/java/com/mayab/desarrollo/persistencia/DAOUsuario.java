package com.mayab.desarrollo.persistencia;
import java.util.List;

import org.hibernate.Session;

import com.mayab.desarrollo.entities.Usuario;
import com.mayab.desarrollo.main.HibernateUtil;

public class DAOUsuario implements IDAOUsuario{
        
    @Override
    public int insertarUsuario(Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        int id = (int)session.save(usuario);
        session.getTransaction().commit();
		session.close();
        return id;

    }

    @Override
    public Usuario consultarUsuario(int idUsuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Usuario usuario = session.get(Usuario.class, idUsuario);
        session.getTransaction().commit();
        session.close();
        return usuario;
    }

    @Override
    public boolean actualizaUsuario(int id, String nombreNuevo, String  contrasenaNueva, String emailNuevo){
        Usuario usuarioOriginal = this.consultarUsuario(id);
        Usuario usuarioModificar = this.consultarUsuario(id);
        Session session = HibernateUtil.getSessionFactory().openSession();    
        session.beginTransaction();
        usuarioModificar.setNombre(!nombreNuevo.isBlank() ? nombreNuevo : usuarioModificar.getNombre());
		usuarioModificar.setPassword(!contrasenaNueva.isBlank() ? contrasenaNueva : usuarioModificar.getPassword());
		usuarioModificar.setEmail(!emailNuevo.isBlank() ? emailNuevo : usuarioModificar.getEmail());
        session.update(usuarioModificar);
        for (Usuario user :  listarTodosLosUsuarios()){
            if(!user.equals(usuarioOriginal) && (user.getNombre().equals(usuarioModificar.getNombre()) || user.getEmail().equals(usuarioModificar.getEmail()))){
                session.getTransaction().rollback();
                session.close();
                return false;
            }
        }
        session.getTransaction().commit();
        session.close();
        return !usuarioModificar.equals(usuarioOriginal);
        
    }

    @Override
    public boolean borrarUsuario(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Usuario usuarioBorrar = this.consultarUsuario(id);
        session.delete(usuarioBorrar);
        session.getTransaction().commit();
        session.close();
        return this.consultarUsuario(id) == null;
    }

  

    @Override
    public List<Usuario> listarTodosLosUsuarios() {
       
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<Usuario> todosUsuarios = session.createQuery("FROM Usuario", Usuario.class).getResultList();
        session.getTransaction().commit();
        session.close();
        return todosUsuarios;
    }

    @Override
    public Usuario validarUsuario(String usuario, String email) {
        List<Usuario> usuarios = listarTodosLosUsuarios();
    
        for (Usuario user : usuarios) {
            if(user.getNombre().equals(usuario) || user.getEmail().equals(email) ){
                return user;
            }
        }

        return null;
    }
    
}