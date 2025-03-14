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
        createTables();
        insereDadosIniciais();
    }

    private void createTables() {
        if (!tableExists("logs")) {
            createLogsTable();
        }
        if (!tableExists("user")) {
            createUserTable();
        }
        if (!tableExists("role")) {
            createRoleTable();
        }
        if (!tableExists("user_role")) {
            createUserRoleTable();
        }
        if (!tableExists("card")) {
            createCardTable();
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
                "route VARCHAR(255)," +
                "message TEXT," +
                "request_body TEXT," +
                "url_params TEXT," +
                "response_body TEXT" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createUserTable() {
        String sql = "CREATE TABLE user (" +
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

    private void createUserRoleTable() {
        String sql = "CREATE TABLE user_role (" +
                "user_id BIGINT," +
                "role_id BIGINT," +
                "PRIMARY KEY (user_id, role_id)," +
                "FOREIGN KEY (user_id) REFERENCES user(id)," +
                "FOREIGN KEY (role_id) REFERENCES role(id)" +
                ");";
        jdbcTemplate.execute(sql);
    }

    private void createCardTable() {
        String sql = "CREATE TABLE card (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "number VARCHAR(512)," +
                "card_key VARCHAR(128) UNIQUE," +
                "user_id BIGINT," +
                "holder_name VARCHAR(512)," +
                "expiration_date DATE," +
                "cvv VARCHAR(128)," +
                "FOREIGN KEY (user_id) REFERENCES user(id)" +
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
        jdbcTemplate.execute("INSERT INTO user (username, password, email) SELECT 'admin', '" + hashedPassword + "', 'admin@example.com' WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'admin')");
        jdbcTemplate.execute("INSERT INTO user_role (user_id, role_id) SELECT u.id, r.id FROM user u, role r WHERE u.username = 'admin' AND r.name = 'ADMIN' AND NOT EXISTS (SELECT 1 FROM user_role ur WHERE ur.user_id = u.id AND ur.role_id = r.id)");
    }

    private void insertUserUser() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode("user");
        jdbcTemplate.execute("INSERT INTO user (username, password, email) SELECT 'user', '" + hashedPassword + "', 'user@example.com' WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'user')");
        jdbcTemplate.execute("INSERT INTO user_role (user_id, role_id) SELECT u.id, r.id FROM user u, role r WHERE u.username = 'user' AND r.name = 'USER' AND NOT EXISTS (SELECT 1 FROM user_role ur WHERE ur.user_id = u.id AND ur.role_id = r.id)");
    }
}