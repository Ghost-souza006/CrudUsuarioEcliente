/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.DAO;

import static br.ulbra.DAO.AbstractDAO.getConnection;
import br.ulbra.Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author aluno.saolucas
 */

 

public class UsuarioDAO extends AbstractDAO implements CrudRepository<Usuario>{

     private String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }
    @Override
    public void salvar(Usuario u) throws SQLException {
       String sql = "INSERT INTO dbusuario (nome, login, senha, ativo) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getSenha());
            ps.setBoolean(4, u.isAtivo());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao inserir Usuario.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
         String sql = "SELECT id, nome, login, senha, ativo FROM dbusuario WHERE id = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("login"),
                            rs.getString("senha"),
                            rs.getString("ativo")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        String sql="select id, nome, login, senha, ativo FROM dbusuario ORDER by id";
        List<Usuario> lista = new ArrayList<>();
        try(Connection con = getConnection();  PreparedStatement ps = con.prepareStatement(sql); 
                ResultSet rs= ps.executeQuery()){  //aqui mosrtra e não modifica
             while(rs.next()){
                Usuario u = new Usuario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("login"),
                rs.getString("senha"),
                rs.getBoolean("ativo")
                );
                lista.add(u);

             }
             
        }
        return lista;
    }

    @Override
    public void atualizar(Usuario u) throws SQLException {
        String sql="UPDATE dbusuario set nome= ?, login=?, senha=?, ativo=?, where id=?";
        try(Connection con= getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,u.getNome());
            ps.setString(2,u.getLogin());
            ps.setString(3,u.getSenha());
            ps.setBoolean(4,u.isAtivo());
            ps.setInt(5,u.getId());
            ps.executeUpdate();  //mexe, modifica os objetos
        }
    }

    @Override
    public void remover(int id) throws SQLException {
         String sql = "DELETE FROM dbusuario WHERE id=?";
        try(Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public Usuario autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT id, login, senha, nome, ativo FROM usuario WHERE login = ? AND ativo = 1";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("senha");
                    if (BCrypt.checkpw(senha, hash)) {
                        return new Usuario(
                            rs.getInt("id"),
                            rs.getString("login"),
                            hash,
                            rs.getString("nome"),
                            rs.getBoolean("ativo")
                        );
                    }
                }
            }
        }
        return null;
    }
      public boolean existeLogin(String login) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}