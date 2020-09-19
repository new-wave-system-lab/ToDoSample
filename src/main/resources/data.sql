/* ユーザー追加 */
INSERT INTO to_do_user(username, password, nickname, role)
  VALUES('admin', 
  '$2a$10$7XDJKECiR0LbKu6o1EsYqeJ6WY.IEW4DV1P5QWFsPnp66jaYfH5/i',
  'admin_nick', 'ADMIN');
INSERT INTO to_do_user(username, password, nickname, role)
  VALUES('user', 
  '$2a$10$7XDJKECiR0LbKu6o1EsYqeJ6WY.IEW4DV1P5QWFsPnp66jaYfH5/i',
  'user_nick', 'USER');

/* フォルダ追加 */
INSERT INTO directory(name, user_id) VALUES('ディレクトリ1', 1);
INSERT INTO directory(name, user_id) VALUES('ディレクトリ2', 1);
INSERT INTO directory(name, user_id) VALUES('ディレクトリ3', 1);
INSERT INTO directory(name, user_id) VALUES('ディレクトリ4', 2);

/* ToDo追加 */
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo1', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo2', 2, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo3', 3, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo4', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo5', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo6', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo7', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo8', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo9', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo10', 1, 1, 1);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo11', 1, 1, 2);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo12', 1, 1, 3);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo13', 1, 2, 4);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo14', 1, 2, 4);
INSERT INTO to_do(name, status, user_id, directory_id) VALUES('ToDo15', 1, 2, 4);
