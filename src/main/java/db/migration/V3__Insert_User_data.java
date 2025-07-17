package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.util.List;

public class V3__Insert_User_data extends BaseJavaMigration {

    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(Context context) throws Exception {
        DataSource dataSource = new SingleConnectionDataSource(context.getConnection(), true);
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        insertData(
                "INSERT INTO users (username, email, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)",
                List.of(
                        new Object[]{"admin", "admin@gmail.com", passwordEncoder.encode("1234"), "Sutthirak", "Sutsaenya"},
                        new Object[]{"admin2", "admin2@gmail.com", passwordEncoder.encode("1234"), "Sutthirak2", "Sutsaenya2"}
                )
        );
    }

    private void insertData(String query, List<Object[]> data) {
        jdbcTemplate.batchUpdate(query, data);
    }


}
