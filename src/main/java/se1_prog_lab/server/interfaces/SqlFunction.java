package se1_prog_lab.server.interfaces;

import java.sql.SQLException;

public interface SqlFunction<T, R> {
    R apply(T t) throws SQLException;
}
