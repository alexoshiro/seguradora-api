# API Seguradora

Api seguradora

## Pré-requisitos

Java versão 11 ou compatíveis

Maven versão 3.6 ou compatíveis

Docker versão 19 ou compatíveis

## Executar com docker-compose

Existem as versões release das imagens da aplicação hospedada no repositório do docker hub:

https://hub.docker.com/r/alexoshiro/seguradora-api

Com isso basta navegar até /seguradora-api e executar o comando:

```
docker-compose up -d
```

Esse comando fará com que o docker realize o pull da imagem do repositório e implante os containers com as imagens do mongodb e seguradora-api.

O acesso a api estará disponível em:

[http://localhost:8080](http://localhost:8080)

A documentação da API(Swagger) estará disponível em:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)



## Ambiente de dev

A aplicação está preparada com configurações básicas para executar em um ambiente de desenvolvimento.

Executar banco de dados:

```
docker-compose -f docker-compose-dev.yml up -d
```

Esse docker compose executa apenas o container do mongodb.

Para executar a aplicação, utilize o perfil **dev** e sua ide de preferência.

### Compilar

Compilando e criando local docker image da api

Navegue até /seguradora-api e execute o seguinte comando:

```
mvn clean install
```

## API - Registros no banco de dados (migration)

A aplicação possui um sistema de *migration* ativo.

Os scripts podem ser visualizados no pacote: project.alexoshiro.seguradora.migration.changelogs

Quando a aplicação é executada com os perfis de **dev** ou **test** ativos, a *migration* gera:

- 1000 registros de mock de clientes na *collection client*(*ChangeSet* id 1605396693) para testes.
- 500 registros de mock de apólices na *collection insurance_policies*(*ChangeSet* id 1605396702) para testes.

Adicionalmente quando a aplicação é executada com o perfil de **test**, a *migration* gera:

- 1 registro de mock de cliente na *collection client* e 3 registros de apólices na *collection insurance_policies*(*ChangeSet* id 1605397192) para a realização dos testes de integração.

## Possíveis erros e soluções

### Erro - Could not build image

Esse erro ocorre quando o plugin dockerfile-maven-plugin não consegue conexão com o docker da máquina host.

Solução: Expose daemon on tcp://localhost:2375 without TLS

**Atenção** - essa solução abre vulnerabilidades, use com cuidado.
