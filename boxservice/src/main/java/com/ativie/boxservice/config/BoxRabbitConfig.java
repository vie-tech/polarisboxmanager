package com.ativie.boxservice.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoxRabbitConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }



    @Bean
    public TopicExchange boxExchange() {
        return new TopicExchange("box_exchange");
    }

    @Bean
    public Queue createBoxQueue() {
        return new Queue("box.create.queue");
    }

    @Bean
    public Binding createBoxBinding() {
        return BindingBuilder.bind(createBoxQueue()).to(boxExchange()).with(
                "box.create");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
      public SimpleRabbitListenerContainerFactory listenerContainerFactory(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter){
          SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
          factory.setConnectionFactory(connectionFactory);
          factory.setMessageConverter(messageConverter());
          return factory;
      }

}
