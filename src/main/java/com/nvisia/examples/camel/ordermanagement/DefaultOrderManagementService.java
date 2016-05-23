package com.nvisia.examples.camel.ordermanagement;

import org.springframework.stereotype.*;

/**
 * Default implementation for order management.
 * 
 * @author Michael Hoffman, NVISIA
 *
 */
@Service("orderManagementService")
public class DefaultOrderManagementService implements OrderManagementService {

   @Override
   public Order processBookOrder(OrderForm orderForm) {
      return new Order(orderForm.getOrderDate(), orderForm.getCustomer(),
            orderForm.getCatalogItem(), "B-112345-" + orderForm.getCustomer().getId());
   }

   @Override
   public Order processClothingOrder(OrderForm orderForm) {
      return new Order(orderForm.getOrderDate(), orderForm.getCustomer(),
            orderForm.getCatalogItem(), "C-462322-" + orderForm.getCustomer().getId());
   }

   @Override
   public Order processElectronicsOrder(OrderForm orderForm) {
      return new Order(orderForm.getOrderDate(), orderForm.getCustomer(),
            orderForm.getCatalogItem(), "E-999991-" + orderForm.getCustomer().getId());
   }

}
