package com.example.documentApp.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface UserDAO {

    @SqlUpdate("create table if not exists login (username varchar(100) primary key, password varchar(100) not null, role varchar(100) not null)")
    void createLoignTable();

    @SqlUpdate("insert into login (username, password, role) values (:username, :password, :role)")
    void insert(@Bind("username") String username, @Bind("password") String password, @Bind("role") String role);

    @SqlUpdate("delete from login where username = :username")
    void delete(@Bind("username") String username);

    @SqlQuery("select username from login where username = :username and password = :password")
    String findUsernamePasswordCombination(@Bind("username") String username, @Bind("password") String password );

    @SqlQuery("select username from login where username = :username")
    String findUsername(@Bind("username") String username);

    @SqlQuery("select role from login where username = :username")
    String findRoleFor(@Bind("username") String username);

}
