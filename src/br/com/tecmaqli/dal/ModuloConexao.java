/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.tecmaqli.dal;
import java.sql.*;
/**
 *
 * @author marco
 */
public class ModuloConexao {
    // metodo para estabelecer conexao com o banco
     public static Connection conector(){
     Connection conexao = null;
     String driver = "com.mysql.cj.jdbc.Driver";
     //armazenando informação do banco de dados
     String url = "jdbc:mysql://localhost:3306/dbtecmaqli";
     String user = "root";
     String password = "aa3232fx.+A";
     //estabelecendo conexao com banco de dados
     try {                         
         Class.forName(driver);
         conexao = DriverManager.getConnection(url, user, password);
         return conexao;
     } catch (Exception e){
         System.out.println(e);
         return null;
     }
   }
}     
 