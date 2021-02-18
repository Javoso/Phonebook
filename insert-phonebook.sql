
INSERT INTO phonebook.usuario
(USUARIO_EMAIL, USUARIO_NOME, USUARIO_SENHA)
VALUES('user@teste.com', 'Usuário', '202cb962ac59075b964b07152d234b70');
INSERT INTO phonebook.usuario
(USUARIO_EMAIL, USUARIO_NOME, USUARIO_SENHA)
VALUES('admin@teste.com', 'Administrador', '202cb962ac59075b964b07152d234b70');


INSERT INTO phonebook.permissao
(PERMISSAO_NOME)
VALUES('ADMIN');
INSERT INTO phonebook.permissao
(PERMISSAO_NOME)
VALUES('USER');


INSERT INTO phonebook.usuario_permissao
(USUARIO_ID, PERMISSAO_ID)
VALUES(1, 1);
INSERT INTO phonebook.usuario_permissao
(USUARIO_ID, PERMISSAO_ID)
VALUES(2, 1);
INSERT INTO phonebook.usuario_permissao
(USUARIO_ID, PERMISSAO_ID)
VALUES(2, 2);


