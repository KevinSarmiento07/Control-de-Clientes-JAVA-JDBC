/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import static datos.Conexion.*;
import domain.ClienteDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ClienteDAOImple implements ClienteDAO {

    private Connection conexionTransaccional = null;

    public ClienteDAOImple() {
    }

    public ClienteDAOImple(Connection conexionTransaccional) {
        this.conexionTransaccional = conexionTransaccional;
    }

    private static final String SQL_SELECT = "SELECT id_cliente, nombre, apellido, email, telefono, saldo FROM control_clientes.cliente";
    private static final String SQL_SELECT_BY_ID = "SELECT id_cliente, nombre, apellido, email, telefono, saldo FROM control_clientes.cliente  WHERE id_cliente = ?";
    private static final String SQL_INSERT = "INSERT INTO control_clientes.cliente(nombre, apellido, email, telefono, saldo) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE control_clientes.cliente SET nombre= ?, apellido= ?, email= ?, telefono= ?, saldo = ? WHERE id_cliente = ?";
    private static final String SQL_DELETE = "DELETE FROM control_clientes.cliente WHERE id_cliente = ?";

    @Override
    public int insertar(ClienteDTO clienteDTO) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int registros = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            ps = conn.prepareStatement(SQL_INSERT);
            ps.setString(1, clienteDTO.getNombre());
            ps.setString(2, clienteDTO.getApellido());
            ps.setString(3, clienteDTO.getEmail());
            ps.setString(4, clienteDTO.getTelefono());
            ps.setDouble(5, clienteDTO.getSaldo());

            registros = ps.executeUpdate();
        } finally {
            try {
                Conexion.close(ps);
                if (this.conexionTransaccional == null) {
                    Conexion.close(conn);
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }

        }

        System.out.println("Insercion Realizada con Exito.");

        return registros;

    }

    @Override
    public List<ClienteDTO> seleccionar() throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClienteDTO clienteDTO = null;
        List<ClienteDTO> clientesDTO = new ArrayList<>();

        try {

            conn = this.conexionTransaccional != null ? this.conexionTransaccional : getConnection();
            ps = conn.prepareStatement(SQL_SELECT);
            rs = ps.executeQuery();

            while (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                double saldo = rs.getDouble("saldo");

                clienteDTO = new ClienteDTO(idCliente, nombre, apellido, email, telefono, saldo);
                clientesDTO.add(clienteDTO);
            }

        } finally {

            try {
                Conexion.close(rs);
                Conexion.close(ps);

                if (this.conexionTransaccional == null) {
                    Conexion.close(conn);
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }

        }

        return clientesDTO;
    }

    public ClienteDTO seleccionarById(ClienteDTO clienteDTO) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            conn = this.conexionTransaccional != null ? this.conexionTransaccional : getConnection();
            ps = conn.prepareStatement(SQL_SELECT_BY_ID,ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1, clienteDTO.getId());
            rs = ps.executeQuery();

            rs.absolute(1);
            int idCliente = rs.getInt("id_cliente");
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String email = rs.getString("email");
            String telefono = rs.getString("telefono");
            double saldo = rs.getDouble("saldo");

            clienteDTO.setId(idCliente);
            clienteDTO.setNombre(nombre);
            clienteDTO.setApellido(apellido);
            clienteDTO.setEmail(email);
            clienteDTO.setTelefono(telefono);
            clienteDTO.setSaldo(saldo);

        } finally {
            close(rs);
            close(ps);
            if (this.conexionTransaccional == null) {
                close(conn);
            }
        }

        System.out.println("Se envio info de select por id");
        return clienteDTO;
    }

    @Override
    public int eliminar(ClienteDTO clienteDTO) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;
        int registros = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : getConnection();
            ps = conn.prepareStatement(SQL_DELETE);

            ps.setInt(1, clienteDTO.getId());

            registros = ps.executeUpdate();
        } finally {

            close(ps);
            if (this.conexionTransaccional == null) {
                close(conn);
            }

        }

        return registros;

    }

    @Override
    public int update(ClienteDTO clienteDTO) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;

        int registros = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : getConnection();
            ps = conn.prepareStatement(SQL_UPDATE);

            ps.setString(1, clienteDTO.getNombre());
            ps.setString(2, clienteDTO.getApellido());
            ps.setString(3, clienteDTO.getEmail());
            ps.setString(4, clienteDTO.getTelefono());
            ps.setDouble(5, clienteDTO.getSaldo());
            ps.setInt(6, clienteDTO.getId());

            registros = ps.executeUpdate();
        } finally {
            close(ps);
            if (this.conexionTransaccional == null) {
                close(conn);
            }

        }

        return registros;

    }

}
