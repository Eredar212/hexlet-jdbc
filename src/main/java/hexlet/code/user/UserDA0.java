package hexlet.code.user;

import hexlet.code.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// Импорт User

public class UserDA0 {
    private Connection connection;

    public UserDA0(Connection conn) {
        connection = conn;
    }

    public void save(User user) throws SQLException {
        // Если пользователь новый, выполняем вставку
        // Иначе обновляем
        if (user.getId() == null) {
            var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();
                // Если идентификатор сгенерирован, извлекаем его и добавляем в сохраненный объект
                if (generatedKeys.next()) {
                    // Обязательно устанавливаем id в сохраненный объект
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            // Здесь код обновления существующей записи
        }
    }

    // Возвращается Optional<User>
    // Это упрощает обработку ситуаций, когда в базе ничего не найдено
    public Optional<User> find(Long id) throws SQLException {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var username = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                var user = new User(username, phone);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

    // Удаляем пользователя
    public void delete(Long id) throws SQLException {
        var deleteSql = "DELETE FROM users WHERE id=?";
        try (var DelState = connection.prepareStatement(deleteSql)) {
            DelState.setLong(1, id);
            DelState.executeUpdate();
        }
    }

    public Optional<List<User>> getEntities() throws SQLException {
        var sql = "SELECT * FROM users";
        List<User> result = new ArrayList<>();
        try (var stmt = connection.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                var username = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                var course = new User(username, phone);
                result.add(course);
            }
            return Optional.of(result);
        }
    }
}