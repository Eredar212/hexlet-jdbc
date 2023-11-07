package hexlet.code;

import hexlet.code.user.UserDA0;

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

        var tommy = new User("Tommy", "111111");
        var johny = new User("Johny", "222222");

        var userDa0 = new UserDA0(conn);

        userDa0.save(tommy);
        userDa0.save(johny);
        printUsers(conn);
        System.out.println();

        System.out.println(userDa0.find(1L));
        System.out.println(userDa0.find(2L));
        System.out.println(userDa0.find(3L));

        userDa0.delete(2L);
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
