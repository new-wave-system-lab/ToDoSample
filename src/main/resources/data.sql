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