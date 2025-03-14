## Configuração do Banco de Dados MySQL com Docker

Para configurar o banco de dados MySQL usando Docker, siga estas etapas:

1.  **Obtenha a imagem Docker do MySQL:**

    ```bash
    docker pull mysql
    ```

2.  **Execute o contêiner MySQL com as configurações necessárias:**

    ```bash
    docker run --name hyperativa-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=DesafioHyperativa -e MYSQL_USER=admin -e MYSQL_PASSWORD=admin -p 3306:3306 -d mysql:latest
    ```

    * `--name hyperativa-mysql`: Define o nome do contêiner como "hyperativa-mysql".
    * `-e MYSQL_ROOT_PASSWORD=password`: Define a senha do usuário root como "password".
    * `-e MYSQL_DATABASE=DesafioHyperativa`: Cria um banco de dados chamado "DesafioHyperativa".
    * `-e MYSQL_USER=admin`: Cria um usuário chamado "admin".
    * `-e MYSQL_PASSWORD=admin`: Define a senha do usuário "admin" como "admin".
    * `-p 3306:3306`: Mapeia a porta 3306 do contêiner para a porta 3306 do host.
    * `-d mysql:latest`: Executa o contêiner em modo detached (em segundo plano) usando a versão mais recente da imagem MySQL.

3.  **Conecte-se ao MySQL usando o cliente MySQL:**

    ```bash
    docker exec -it hyperativa-mysql mysql -u root -p
    ```

    * `docker exec -it hyperativa-mysql`: Executa um comando interativo dentro do contêiner "hyperativa-mysql".
    * `mysql -u root -p`: Inicia o cliente MySQL e solicita a senha do usuário root.

    Quando solicitado, digite a senha do usuário root ("password").
