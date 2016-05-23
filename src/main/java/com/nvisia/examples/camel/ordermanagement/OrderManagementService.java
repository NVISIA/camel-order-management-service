package com.nvisia.examples.camel.ordermanagement;

/**
 * Interface for services to process orders as part of order management.
 * 
 * @author Michael Hoffman
 *
 */
public interface OrderManagementService {

   /**
    * Processes a book order and returns an order number and the full order.
    * 
    * @param orderForm
    * @return
    */
   Order processBookOrder(OrderForm orderForm);

   /**
    * Processes a clothing order and returns an order number and the full
    * number.
    * 
    * @param orderForm
    * @return
    */
   Order processClothingOrder(OrderForm orderForm);

   /**
    * Processes an electronics order and returns an order number and the full
    * number.
    * 
    * @param orderForm
    * @return
    */
   Order processElectronicsOrder(OrderForm orderForm);

}
