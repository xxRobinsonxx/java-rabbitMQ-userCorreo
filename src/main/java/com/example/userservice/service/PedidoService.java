package com.example.userservice.service;

import com.example.userservice.messaging.MessagePublisher;
import com.example.userservice.model.EmailNotification;
import com.example.userservice.model.Pedido;
import com.example.userservice.model.User;
import com.example.userservice.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PedidoRepository pedidoRepository;
    private final MessagePublisher messagePublisher;

    public PedidoService(PedidoRepository pedidoRepository, MessagePublisher messagePublisher) {
        this.pedidoRepository = pedidoRepository;
        this.messagePublisher = messagePublisher;
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido createPedido(Pedido pedido) {
       // Save user to database
        Pedido savedPedido = pedidoRepository.save(pedido);
        logger.info("Pedido created successfully: {}", savedPedido.getId());

        try {
            // Create email notification
            EmailNotification notification = EmailNotification.forNewPedidoRegistration(savedPedido);

            // Send notification to RabbitMQ (o simplemente registra si RabbitMQ no está disponible)
            messagePublisher.publishEmailNotification(notification);
        } catch (Exception e) {
            // Log the error but don't fail the user creation
            logger.error("Failed to process email notification: {}", e.getMessage());
        }

        return savedPedido;
    }

    @Transactional
    public Optional<Pedido> updatePedido(Long id, Pedido pedidoDetails) {
        return pedidoRepository.findById(id)
                .map(existingPedido -> {
                    existingPedido.setAddress(pedidoDetails.getAddress());
                    existingPedido.setRuc(pedidoDetails.getRuc());

                    // Email cannot be changed in this example
                    // If changed, we would need to validate it's not already in use

                    return pedidoRepository.save(existingPedido);
                });
    }

    @Transactional
    public boolean deletePedido(Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedidoRepository.delete(pedido);
                    logger.info("Pedido deleted: {}", id);
                    return true;
                })
                .orElse(false);
    }
}