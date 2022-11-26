/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import datos.ClienteDAO;
import datos.ClienteDAOImple;
import datos.Conexion;
import domain.ClienteDTO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kevin
 */
public class ClienteTest {
    
    
    public static void main(String[] args) {
        
        Connection conexion = null;
        
        try {
            conexion = Conexion.getConnection();
            
            if(conexion.getAutoCommit()){
                conexion.setAutoCommit(false);
            }
            
            
            
            ClienteDAO clienteDAO = new ClienteDAOImple(conexion);
            
            ClienteDTO clienteDTO = new ClienteDTO(3);
            clienteDAO.seleccionarById(clienteDTO);
            System.out.println(clienteDTO);
            
            ClienteDTO clienteDTOMod = new ClienteDTO(2, "AleMod", "Aradna", "ale@gmail.com", "332211", 500);
            
//            clienteDAO.update(clienteDTOMod);
//            List<ClienteDTO> clientesDTO = new ArrayList<>();
//            
//            clientesDTO = clienteDAO.seleccionar();
//            
//            for(ClienteDTO clienteDTO : clientesDTO){
//                System.out.println("Cliente DTO: " + clienteDTO);
//            }
            
             ClienteDTO clienteDTOInser = new ClienteDTO("Leonidas", "Roma", "leo@gmail.com", "12434", 100.0);
             
             clienteDAO.insertar(clienteDTOInser);
            conexion.commit();
            System.out.println("Se ha hecho un commit de la transaccion");
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al Rollback");
            
            
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        }
        
    }
}
