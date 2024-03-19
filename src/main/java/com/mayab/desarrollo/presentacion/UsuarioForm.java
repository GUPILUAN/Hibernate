package com.mayab.desarrollo.presentacion;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mayab.desarrollo.entities.Usuario;
import com.mayab.desarrollo.logica.ControllerUsuario;
import com.mayab.desarrollo.persistencia.DAOUsuario;
public class UsuarioForm extends  JFrame implements ActionListener{
     private JButton botonCreate, botonRead, botonUpdate, botonDelete,botonBorrarVarios , botonObtenerAll;
        private JTextField idText, nombreText, contrasenaText, emailText;
        private JLabel etiquetaID, etiquetaNombre,etiquetaContrasena,etiquetaEmail, etiquetaResultado;
        private ControllerUsuario c;
        
        public UsuarioForm(){
            super("ADMINISTRADOR DE USUARIOS");
            this.c = new ControllerUsuario(new DAOUsuario());
            this.botonCreate = new JButton("Crear usuario");
            this.botonRead = new JButton("Buscar");
            this.botonUpdate = new JButton("Actualizar");
            this.botonDelete = new JButton("Eliminar");
            this.idText = new JTextField("", 10);
            this.nombreText = new JTextField("", 30);
            this.contrasenaText = new JTextField("", 30);
            this.emailText = new JTextField("", 30);
            this.etiquetaID = new JLabel("ID: ");
            this.etiquetaNombre = new JLabel("Nombre: ");
            this.etiquetaContrasena = new JLabel("Contraseña: ");
            this.etiquetaEmail = new JLabel("Email: ");
            this.etiquetaResultado = new JLabel("Ingresa tus  datos");
            this.botonObtenerAll = new JButton("Obtener todos los usuarios");
            this.botonBorrarVarios = new JButton("Borrar varios separados por ,");

            
            
            Container container = getContentPane();
            container.setLayout(new GridLayout(7, 2, 5, 5)); 


            JPanel panelID = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelID.add(etiquetaID);
            panelID.add(idText);
            JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelNombre.add(etiquetaNombre);
            panelNombre.add(nombreText);
            JPanel panelContrasena = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelContrasena.add(etiquetaContrasena);
            panelContrasena.add(contrasenaText);
            JPanel panelEmail = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelEmail.add(etiquetaEmail);
            panelEmail.add(emailText);


            container.add(panelID);
            container.add(panelNombre);
            container.add(panelContrasena);
            container.add(panelEmail);

            container.add(botonCreate);
            container.add(botonRead);
            container.add(botonUpdate);
            container.add(botonDelete);
            container.add(botonObtenerAll);
            container.add(botonBorrarVarios);
            container.add(etiquetaResultado);
            

            botonCreate.addActionListener(this);
            botonRead.addActionListener(this);
            botonUpdate.addActionListener(this);
            botonDelete.addActionListener(this);
            botonObtenerAll.addActionListener(this);
            botonBorrarVarios.addActionListener(this);

            
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            setSize(1920,500);
            //setResizable(false);
            setVisible(true);
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == botonCreate){
                String mensaje;
                if(nombreText.getText().isEmpty() || emailText.getText().isEmpty()){
                    mensaje = "Por lo menos los campos email y nombre deben estar rellenos, intenta nuevamente";
                }else{
                    int id = c.crearUsuario(nombreText.getText(), contrasenaText.getText(), emailText.getText());
                    mensaje = id > 0 ? "Se ha creado con exito el usuario, su ID es: " + id + (contrasenaText.getText().isBlank()? " se creó password predeterminado (se recomienda cambiar)": ""): "No se pudo crear el usuario";
                }
                etiquetaResultado.setText(mensaje);

            }else if(e.getSource() == botonRead){
                Usuario user;
                String mensaje;
                try {
                    int id= Integer.parseInt((String)idText.getText().trim());
                    user = c.obtenerUsuario(id);
                    mensaje = user != null ? String.format("Se ha encontrado con exito el usuario con ID: %d Nombre: %s Email: %s", user.getId(), user.getNombre(),user.getEmail()): "No existe usuario con ese ID";
                } catch (Exception event) {
                    mensaje = "Error al leer id, asegurate de poner un numero entero en el campo id";
                }
                etiquetaResultado.setText(mensaje);

            }else if(e.getSource() == botonUpdate){
                String mensaje;
                int id = -1; 
                try {
                    if(!idText.getText().isBlank()){
                         id= Integer.parseInt((String)idText.getText().trim());
                    }
                    
                    mensaje = c.actualizaUsuario(
                        id, 
                        nombreText.getText(), 
                        contrasenaText.getText(), 
                        emailText.getText()
                    ) ? 
                    !nombreText.getText().isBlank() && !emailText.getText().isBlank() ?  
                    String.format("Se actualizaron los datos para el usuario %s con email %s",  nombreText.getText(), emailText.getText()) :
                    String.format(
                        "Se ha modificado con exito el usuario con %s: %s", 
                        id > 0 ? "id" : (nombreText.getText().isBlank() ? "email" : "nombre"),
                        id > 0 ? id : (nombreText.getText().isBlank() ?  emailText.getText() : nombreText.getText())
                    ) :
                    "No se modifico debido a que no existe, no se realizó algun cambio, o es el mismo email o nombre";
                } catch (Exception event) {
                    mensaje = "Error al leer id, asegurate de poner un numero entero en el campo id";
                }
                etiquetaResultado.setText(mensaje);

            }else if(e.getSource() == botonDelete){
                String mensaje;
                try {
                    int id= Integer.parseInt((String)idText.getText().trim());
                    mensaje =  c.eliminarUsuario(id) ? "Se ha eliminado con exito el usuario con ID: " + id : "No se elimino ningun usuario, quizá no existe el usuario con ese ID";
                } catch (Exception event) {
                    mensaje = "Error al leer id, asegurate de poner un numero entero en el campo id";
                }
                etiquetaResultado.setText(mensaje);

            }else if(e.getSource() == botonObtenerAll){
                JFrame frame = new JFrame();
                JTextArea textArea = new JTextArea(10,30);

                c.obtenerUsuarios().forEach(user -> textArea.append(user.toString() + "\n" ));
        
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                
                frame.getContentPane().add(scrollPane);
                frame.setSize(500, 200);
                frame.setResizable(false);
                frame.setVisible(true);
            }

            else if(e.getSource() == botonBorrarVarios){
                String ids = idText.getText();
                c.borrarMuchosUsuarios(ids);
            }
        }
}
