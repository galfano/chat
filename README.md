## Problema
O sistema usa websockets para enviar notificações para usuários conectados. Em ambientes não clusterizados a configuração 
padrão funciona bem. Entretanto, em ambientes clusterizados com múltiplos nodes da aplicação, obviamente as notificações 
não poderiam atingir todos os usuários porque sua conexão poderia ter sido estabelecida com qualquer node da aplicação, 
possivelmente não com o que envia a mensagem.


## Solução
O RabbitMQ é um software de mensagem intermediário que pode ser integrado com o Spring. Nesse caso a aplicação recebe as
conexões com websockets e as retransmite para o RabbitMQ. Dessa forma, todos os websockets são informados por qualquer node
da aplicação e podem ser notificados.

### Instalação no Ubuntu 18.04

    $ sudo apt-get install rabbitmq-server
    $ sudo rabbitmq-plugins enable rabbitmq_management
    $ sudo rabbitmq-plugins enable rabbitmq_web_stomp
    $ sudo service rabbitmq-server stop
    $ sudo service rabbitmq-server start

Após a instalação acesse o painel do serviço: http://localhost:15672
O login e senha padrão são: guest/guest

## Servidor

Adicione no pom.xml as dependências abaixo:

    <!-- https://mvnrepository.com/artifact/org.springframework.amqp/spring-amqp -->
    <dependency>
        <groupId>org.springframework.amqp</groupId>
        <artifactId>spring-amqp</artifactId>
        <version>2.1.0.RELEASE</version>
    </dependency>
    
    <!-- https://mvnrepository.com/artifact/io.projectreactor.ipc/reactor-netty -->
    <dependency>
        <groupId>io.projectreactor.ipc</groupId>
        <artifactId>reactor-netty</artifactId>
        <version>0.7.10.RELEASE</version>
    </dependency>

Configure a conexão de retransmissão na classe de configuração do Websocket

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app")
                  .enableStompBrokerRelay("/topic")
                  .setRelayHost("localhost")
                  .setRelayPort(61613)
                  .setClientLogin("guest")
                  .setClientPasscode("guest");
    }
    
Por padrão o HabbitMQ não suporta "/" como separador padrão. O separador padrão é o ".". Sendo assim no controlador 
responsável por receber as mensagens enviadas pelo cliente e entregar para os usuários inscritos na URL, a annotation fica assim:

    @SendTo("/topic/messages.{sala}")    

## Cliente

No caso do chat multisalas é necessário passar o id da sala como parâmetro na URL de envio de mensagens e na URL de inscrição
para receber as mensagens. O padrão da aplicação antes da instalação do RabbitMQ para as URL era:

* ENVIAR: /app/messages/{sala}
* RECEBER: /topic/messages/{sala}

Porém o RabbitMQ não suporta "/" como separador por padrão. O separador padrão é o ".". Nesse caso a URL para envio não é afetada,
apenas a URL de inscrição é necessária ser alterada, para:

* RECEBER: /topic/messages.{sala} 






