/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import datos.ClienteDAOImple;
import domain.ClienteDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author kevin
 */
@WebServlet("/ServletControlador")
public class ServetControlador extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion != null) {

            switch (accion) {
                case "editar":
                    this.editarCliente(request, response);
                    break;
                    
                case "eliminar":
                    this.eliminarCliente(request, response);
                    break;

                default:
                    this.accionDefault(request, response);

            }

        } else {
            this.accionDefault(request, response);
        }

    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ClienteDTO> clientes = new ClienteDAOImple().seleccionar();
            System.out.println("Clientes: " + clientes);

            HttpSession session = request.getSession();

            session.setAttribute("clientes", clientes);
            session.setAttribute("totalClientes", clientes.size());
            session.setAttribute("saldoTotal", saldoTotal(clientes));

            //El metodo request, hace que no podamos ver nada del los ususarios ni el saldo, ni el total, debido a que no tiene alcance, debemos darle un alcance ,mayor, co,o session
            //request.setAttribute("clientes", clientes);
            //request.setAttribute("totalClientes", clientes.size());
            //request.setAttribute("saldoTotal", saldoTotal(clientes));
            //La linea de abajo hace que cuando recarguemos la pagina, al reenviar el formulario, duploquemos los registros enviados anteriormente
            //Queremos que la url cambie
            //request.getRequestDispatcher("clientes.jsp").forward(request, response);
            //El send redirect si notifica al navegador
            response.sendRedirect("clientes.jsp");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar":
                    this.insertarCliente(request, response);

                    break;

                case "modificar":
                    this.modificarCliente(request, response);
                    break;

                default:
                    this.accionDefault(request, response);
                    break;
            }
        } else {

            this.accionDefault(request, response);
        }
    }

    private void insertarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Recuperar los valores del formulario agregarCliente
        //El "nombre" del getParameter es el name del input de agregarCliente.jsp
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");

        if (saldoString != null && !saldoString.equals("")) {
            saldo = Double.parseDouble(saldoString);
        }

        //Creamos el objeto cliente para mandarle todos los parametros
        ClienteDTO clienteDTO = new ClienteDTO(nombre, apellido, email, telefono, saldo);

        int registrosModificados;
        try {
            registrosModificados = new ClienteDAOImple().insertar(clienteDTO);
            System.out.println("Registros Modificados: " + registrosModificados);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            this.accionDefault(request, response);
        }

        //Redireccionamos al metodo accion default
        this.accionDefault(request, response);

    }

    private double saldoTotal(List<ClienteDTO> clientes) {

        double suma = 0;

        for (int i = 0; i < clientes.size(); i++) {
            suma += clientes.get(i).getSaldo();
        }

        return suma;

    }

    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            ClienteDTO clienteDTO = new ClienteDAOImple().seleccionarById(new ClienteDTO(id));
            request.setAttribute("cliente", clienteDTO);
            String jspEditar = "/WEB-INF/paginas/cliente/editarClientes.jsp";
            request.getRequestDispatcher(jspEditar).forward(request, response);

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }

    }

    private void modificarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recuperar los valores del formulario agregarCliente
        //El "nombre" del getParameter es el name del input de editarCliente.jsp
        int idCliente = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");

        if (saldoString != null && !saldoString.equals("")) {
            saldo = Double.parseDouble(saldoString);
        }

        //Creamos el objeto cliente para mandarle todos los parametros
        ClienteDTO clienteDTO = new ClienteDTO(idCliente,nombre, apellido, email, telefono, saldo);

        int registrosModificados;
        try {
            
            //Modificar el objeto en la base de datos
            registrosModificados = new ClienteDAOImple().update(clienteDTO);
            System.out.println("Registros Modificados: " + registrosModificados);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            this.accionDefault(request, response);
        }

        //Redireccionamos al metodo accion default
        this.accionDefault(request, response);
        
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        int idCliente = Integer.parseInt(request.getParameter("id"));
        
        ClienteDTO clienteDTO = new ClienteDTO(idCliente);
        
        int registrosEliminados;
        
        try {
            registrosEliminados = new ClienteDAOImple().eliminar(clienteDTO);
            System.out.println("Registros Eliminados: " + registrosEliminados);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        
        this.accionDefault(request, response);


    }
    
    
    
  

}
