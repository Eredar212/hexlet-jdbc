package hexlet.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Создаем соединение с базой в памяти
        // База создается прямо во время выполнения этой строчки
        // Здесь hexlet_test — это имя базы данных
        var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test");

        var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
        // Чтобы выполнить запрос, создадим объект statement
        try (var statement = conn.createStatement()) {
            statement.execute(sql);
        } // В конце закрываем

        var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
        try (var statement2 = conn.prepareStatement(sql2)) {
            statement2.setString(1, "Tommy");
            statement2.setString(2, "111111");
            statement2.executeUpdate();

            statement2.setString(1, "Johny");
            statement2.setString(2, "222222");
            statement2.executeUpdate();
        }

        /*var sql3 = "SELECT * FROM users";
        try (var statement3 = conn.createStatement()) {
            // Здесь вы видите указатель на набор данных в памяти СУБД
            var resultSet = statement3.executeQuery(sql3);
            // Набор данных — это итератор
            // Мы перемещаемся по нему с помощью next() и каждый раз получаем новые значения
            while (resultSet.next()) {
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("phone"));
            }
        }*/
        printUsers(conn);
        System.out.println();

        var deleteSql = "DELETE FROM users WHERE username=?";
        try (var DelState = conn.prepareStatement(deleteSql)) {
            DelState.setString(1, "Johny");
            DelState.executeUpdate();
        }
        printUsers(conn);
        // Закрываем соединение
        conn.close();
    }
    public static void printUsers(Connection conn) throws SQLException {
        var sql3 = "SELECT * FROM users";
        try (var statement3 = conn.createStatement()) {
            // Здесь вы видите указатель на набор данных в памяти СУБД
            var resultSet = statement3.executeQuery(sql3);
            // Набор данных — это итератор
            // Мы перемещаемся по нему с помощью next() и каждый раз получаем новые значения
            while (resultSet.next()) {
                System.out.printf("%s %s\n", resultSet.getString("username"),resultSet.getString("phone"));
            }
        }
    }
}
