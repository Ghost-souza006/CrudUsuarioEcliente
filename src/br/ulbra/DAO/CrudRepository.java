/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.dao;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author qijef
 */
public interface CrudRepository<T> {

    void salvar(T obj) throws SQLException;

    T buscarPorId(int id) throws SQLException;

    List<T> listar() throws SQLException;

    void atualizar(T obj) throws SQLException;

    void remover(int id) throws SQLException;

}
