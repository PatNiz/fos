package com.xformation.persistance.repository;

import com.xformation.model.RestaurantOrder;
import com.xformation.model.OrderDishes;
import com.xformation.model.OrderDrinks;
import com.xformation.persistance.PersistenceManager;
import com.xformation.persistance.QuerySpec;

import java.util.List;
import java.util.Optional;

public class OrderRepository {
    private final PersistenceManager manager;

    public OrderRepository(PersistenceManager manager) {
        this.manager = manager;
    }

    public void save(RestaurantOrder order) {
        try {
            Long orderId = manager.insert(order);
            order.setId(orderId);
            saveOrderDishes(order);
            saveOrderDrinks(order);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving order", e);
        }
    }

    private void saveOrderDishes(RestaurantOrder order) {
        for (Long dishId : order.getDishes()) {
            OrderDishes orderDish = new OrderDishes(order.getId(), dishId);
            manager.insert(orderDish);
        }
    }

    private void saveOrderDrinks(RestaurantOrder order) {
        for (Long drinkId : order.getDrinks()) {
            OrderDrinks orderDrink = new OrderDrinks(order.getId(), drinkId);
            manager.insert(orderDrink);
        }
    }

    public void delete(RestaurantOrder order) {
        try {
            manager.delete(order);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting order", e);
        }
    }

    public List<RestaurantOrder> findAllOrders() {
        return manager.find(new QuerySpec<>(RestaurantOrder.class));
    }

    public Optional<RestaurantOrder> findOrderByUserId(Long userId) {
        QuerySpec<RestaurantOrder> querySpec = new QuerySpec<>(RestaurantOrder.class);
        querySpec.addCondition("user_id", userId);
        List<RestaurantOrder> orders = manager.find(querySpec);
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }

    public Optional<RestaurantOrder> findOrderById(Long id) {
        QuerySpec<RestaurantOrder> querySpec = new QuerySpec<>(RestaurantOrder.class);
        querySpec.addCondition("id", id);
        List<RestaurantOrder> orders = manager.find(querySpec);
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }
}
