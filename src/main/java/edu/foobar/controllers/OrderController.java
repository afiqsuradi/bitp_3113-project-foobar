package edu.foobar.controllers;

import edu.foobar.dao.OrderDao;
import edu.foobar.models.Enums;
import edu.foobar.models.Membership;
import edu.foobar.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderController {

    private final OrderDao orderDao;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static Order order;

    /**
     *
     * @param membershipId membershipId to indicate current order session
     */
    public OrderController(int membershipId) {
        this.orderDao = new OrderDao();
    }

    public static Order getCurrentOrder() throws Exception{
        if(order == null) {
            logger.error("Current order session is empty! Create a new order or retrieve it");
            throw new Exception("Current order session is empty! Create a new order or retrieve it");
        }
        return order;
    }

    public static void setCurrentOrder(Order order) {
        OrderController.order = order;
    }

    public Order getOrder(int id) throws Exception{
        Order order = orderDao.get(id);
        if(order == null){
            logger.warn("Order not found with id: " + id);
        }
        return order;
    }

    public Order getLatestMemberOrder(int membershipId) throws Exception{
        Order order = orderDao.getLatestMemberOrder(membershipId);
        if(order == null){
            logger.warn("Order not found for membership id: " + membershipId);
        }
        return order;
    }


    public List<Order> getAllOrders() throws Exception {
        return orderDao.getAll();
    }

    public Order createOrder(Membership membership, Enums.OrderStatus status) throws Exception {
        Order order = new Order(membership, status);
        return orderDao.save(order);
    }

    public void updateOrder(Order order) throws Exception{
        orderDao.update(order);
    }

    public void deleteOrder(int id) throws Exception {
        Order order = orderDao.get(id);
        if (order != null) {
            orderDao.delete(order);
        } else {
            logger.warn("Order not found with id: " + id);
        }
    }
}