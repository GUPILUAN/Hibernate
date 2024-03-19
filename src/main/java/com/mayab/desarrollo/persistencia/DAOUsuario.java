package com.mayab.desarrollo.persistencia;
import java.util.List;

import org.hibernate.Session;

import com.mayab.desarrollo.entities.Usuario;
import com.mayab.desarrollo.main.HibernateUtil;

public class DAOUsuario implements IDAOUsuario{
        
    @Override
    public int insertarUsuario(Usuario usuario) {
        int hueco = this.obtenerHueco();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.createNativeQuery(String.format("UPDATE hibernate_sequence SET next_val = %d" , hueco)).executeUpdate();
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
        Usuario usuarioBorrar = this.consultarUsuario(id);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        if(usuarioBorrar != null){

            session.delete(usuarioBorrar);
            session.getTransaction().commit(); 
            session.close();
            return true;
        } else {
            session.close();
            return false;
        }
    }
    

  

    @Override
    public List<Usuario> listarTodosLosUsuarios() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<Usuario> todosUsuarios = session.createQuery("FROM Usuario ORDER BY id", Usuario.class).getResultList();
        session.getTransaction().commit();
        
        return todosUsuarios;
    }

    @Override
    public Usuario validarUsuario(String usuario, String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Usuario user = session.createQuery("FROM Usuario WHERE nombre = :usuario OR email = :email", Usuario.class)
                                .setParameter("usuario", usuario)
                                .setParameter("email", email)
                                .uniqueResult();
        session.close();
        return user;
    }

    @Override
    public int obtenerHueco() {
        int hueco = 0;
        List<Usuario> usuarios = listarTodosLosUsuarios();
        int maxId = usuarios.isEmpty() ?  0 : usuarios.getLast().getId();
        if(maxId != 0 && usuarios.size() != maxId){
            for (Usuario user : usuarios) {
                hueco++;
                if(user.getId() != hueco){
                    break;
                }
            }
            return hueco;
        }
        return (maxId + 1);
    }
    
}
