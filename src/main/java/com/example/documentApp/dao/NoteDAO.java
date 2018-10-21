package com.example.documentApp.dao;

import com.example.documentApp.core.Note;
import com.example.documentApp.core.NoteMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(NoteMapper.class)
public interface NoteDAO {
    @SqlUpdate("create table if not exists notes (ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, title varchar(255) not null, content varchar(2000), user varchar(100) not null)")
    void createNoteTable();

    @SqlUpdate("insert into notes (title, content, user) values (:title, :content, :user)")
    void insert(@Bind("title") String title, @Bind("content") String content, @Bind("user") String user);

    @SqlQuery("select id, title from notes where user = :username")
    List<Note> getNoteTitlesFor(@Bind("username") String username);

    @SqlQuery("select id, title, content from notes where id = :id and user = :username")
    Note getNoteFor(@Bind("id") int id, @Bind("username") String username);

    @SqlUpdate("update notes set title = :newTitle, content = :newContent where id = :id and user = :username")
    void updateNote(@Bind("id") int id, @Bind("username") String username, @Bind("newTitle") String title, @Bind("newContent") String content);

    @SqlUpdate("delete from notes where id = :id and user = :username")
    void deleteNote(@Bind("id") int id, @Bind("username") String username);

    @SqlUpdate("delete from notes where user = :username")
    void deleteNotes(@Bind("username") String username);

}
