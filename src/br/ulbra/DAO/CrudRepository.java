/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.DAO;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author aluno.saolucas
 */
public interface CrudRepository<T> {

    public void salvar(T obj) throws SQLException;

    public T buscarPorId(int id) throws SQLException;

    public List<T> listar() throws SQLException;

    public void atualizar(T obj) throws SQLException;

    public void remover(int id) throws SQLException;
}