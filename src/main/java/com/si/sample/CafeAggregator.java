package com.si.sample;

import com.si.sample.models.Delivery;
import com.si.sample.models.Dish;
import com.si.sample.models.Drink;
import org.omg.CORBA.Object;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CafeAggregator {

    @Aggregator
    public Delivery output(List<Object> objects) {
        Delivery delivery = new Delivery();
        for (Object o : objects) {
            if (o instanceof Drink) {
                delivery.setOrderNumber(((Drink) o).getOrderNumber());
                delivery.setDrinkName(((Drink) o).getDrinkName());
            } else {
                delivery.setDishName(((Dish) o).getDishName());
            }
        }

        return delivery;
    }

    @ReleaseStrategy
    public boolean releaseChecker(List<Message<Object>> messages) {
        return messages.size() == 2;
    }

    @CorrelationStrategy
    public Integer correlation(Object object) {
        if (object instanceof Drink) {
            return ((Drink) object).getOrderNumber();
        } else {
            return ((Dish) object).getOrderNumber();
        }

    }

}