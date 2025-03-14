package com.avaliacao.desafioHyperativa.config.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initializeDatabase() {
        criarTabelas();
        insereDadosIniciais();
    }

    private void criarTabelas() {
        if (!tableExists("logs")) {
            createLogsTable();
        }
        if (!tableExists("usuario")) {
            createUsuarioTable();
        }
        if (!tableExists("role")) {
            createRoleTable();
        }
        if (!tableExists("usuario_role")) {
            createUsuarioRoleTable();
        }
        if (!tableExists("cartao")) {
            createCartaoTable();
        }
    }

    private void insereDadosIniciais() {
        insertRoles();
        insertAdminUser();
        insertUserUser();
    }

    private boolean tableExists(String tableName) {
        try {
            String sql = "SHOW TABLES LIKE ?";
            List<String> tables = jdbcTemplate.queryForList(sql, String.class, tableName);
            return !tables.isEmpty();
        } catch (Exception e) {
            System.err.println("Erro ao verificar se a tabela existe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void createLogsTable() {
        String sql = "CREATE TABLE logs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "timestamp DATETIME," +
                "level VARCHAR(255)," +
                "message TEXT," +
                "requestBody TEXT," +
                "urlParams TEXT," +
                "responseBody TEXT" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createUsuarioTable() {
        String sql = "CREATE TABLE usuario (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(255)," +
                "password VARCHAR(255)," +
                "email VARCHAR(255)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createRoleTable() {
        String sql = "CREATE TABLE role (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createUsuarioRoleTable() {
        String sql = "CREATE TABLE usuario_role (" +
                "usuario_id BIGINT," +
                "role_id BIGINT," +
                "PRIMARY KEY (usuario_id, role_id)," +
                "FOREIGN KEY (usuario_id) REFERENCES usuario(id)," +
                "FOREIGN KEY (role_id) REFERENCES role(id)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createCartaoTable() {
        String sql = "CREATE TABLE cartao (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "numero VARCHAR(255)," +
                "usuario_id BIGINT," +
                "FOREIGN KEY (usuario_id) REFERENCES usuario(id)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void insertRoles() {
        jdbcTemplate.execute("INSERT INTO role (name) SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ADMIN')");
        jdbcTemplate.execute("INSERT INTO role (name) SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'USER')");
    }

    private void insertAdminUser() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode("admin");
        jdbcTemplate.execute("INSERT INTO usuario (username, password, email) SELECT 'admin', '" + hashedPassword + "', 'admin@example.com' WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'admin')");
        jdbcTemplate.execute("INSERT INTO usuario_role (usuario_id, role_id) SELECT u.id, r.id FROM usuario u, role r WHERE u.username = 'admin' AND r.name = 'ADMIN' AND NOT EXISTS (SELECT 1 FROM usuario_role ur WHERE ur.usuario_id = u.id AND ur.role_id = r.id)");
    }

    private void insertUserUser() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode("user");
        jdbcTemplate.execute("INSERT INTO usuario (username, password, email) SELECT 'user', '" + hashedPassword + "', 'user@example.com' WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE username = 'user')");
        jdbcTemplate.execute("INSERT INTO usuario_role (usuario_id, role_id) SELECT u.id, r.id FROM usuario u, role r WHERE u.username = 'user' AND r.name = 'USER' AND NOT EXISTS (SELECT 1 FROM usuario_role ur WHERE ur.usuario_id = u.id AND ur.role_id = r.id)");
    }
}