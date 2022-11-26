/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import domain.ClienteDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author kevin
 */
public interface ClienteDAO {
    
    public int insertar(ClienteDTO clienteDTO) throws SQLException;
    
    public List<ClienteDTO> seleccionar()throws SQLException;
    
    public ClienteDTO seleccionarById(ClienteDTO clienteDTO) throws SQLException;
    
    public int eliminar(ClienteDTO clienteDTO) throws SQLException;
    
    public int update(ClienteDTO clienteDTO)throws SQLException;
}
