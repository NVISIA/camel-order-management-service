package com.nvisia.examples.camel.ordermanagement;

import java.util.*;

import org.apache.camel.component.servlet.*;
import org.apache.camel.model.rest.*;
import org.apache.camel.spring.boot.*;
import org.apache.camel.swagger.servlet.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.embedded.*;
import org.springframework.context.annotation.*;

/**
 * Spring boot application that defines the routes available for order
 * management.
 * 
 * @author Michael Hoffman, NVISIA
 *
 */
@SpringBootApplication
public class OrderManagementRouter extends FatJarRouter {

   @Override
   public void configure() {
      initializeRestConfiguration();
      initializeOrderManagementRoute();
   }

   protected void initializeRestConfiguration() {
      // Start by building an instance of RestConfigurationDefinition. Need to
      // specify the component we are going to use for enabling REST endpoints,
      // specifically CamelServlet in this case. Set the binding mode to JSON.
      restConfiguration()
            // Leverage the CamelServlet component for the REST DSL
            .component("servlet")
            // Bind using JSON
            .bindingMode(RestBindingMode.json)
            // I like pretty things...
            .dataFormatProperty("prettyPrint", "true")
            // This is the context path to be used for Swagger API documentation
            .apiContextPath("api-doc")
            // Properties for Swagger
            // Title of the API
            .apiProperty("api.title", "Order Management API")
            // Version of the API
            .apiProperty("api.version", "1.0.0")
            // CORS (resource sharing) enablement
            .apiProperty("cors", "true")
            // Use localhost for calls
            .apiProperty("host", "localhost:8082")
            // Set base path
            .apiProperty("base.path", "camel-order-management-service/api");
   }

   protected void initializeOrderManagementRoute() {
      // Definition of the post order endpoint
      rest("/order")
            // This is a POST method call for submitting an order using the
            // order form
            .post()
            // Description of what the method does
            .description("Submit a new order")
            // Define the type used for input
            .type(OrderForm.class)
            // Define the type used for output
            .outType(Order.class)
            // Route direct to process order by type
            .to("direct:routeOrderByType");

      // Definition of the content based routing endpoint
      from("direct:routeOrderByType")
            // Use the Content Based Router EIP to process the order based on
            // the item type
            .choice()
            // Check for Catalog Item as book
            .when()
            // When this is a book, send it to the process book method
            .simple("${body.catalogItem.catalogItemType} == '"
                  + CatalogItemType.BOOK.toString() + "'")
            // Send it to the method for book processing
            .to("bean:orderManagementService?method=processBookOrder(${body})")
            // Check for Catalog Item as clothing
            .when()
            // When this is clothing, send it to the process clothing method
            .simple("${body.catalogItem.catalogItemType} == '"
                  + CatalogItemType.CLOTHING.toString() + "'")
            // Send it to the method for clothing processing
            .to("bean:orderManagementService?method=processClothingOrder(${body})")
            // Check for Catalog Item as electronics
            .when()
            // When this is electronics, send it to the process electronics
            // method
            .simple("${body.catalogItem.catalogItemType} == '"
                  + CatalogItemType.ELECTRONICS.toString() + "'")
            // Send it to the method for electronics processing
            .to("bean:orderManagementService?method=processElectronicsOrder(${body})")
            // Catch other items
            .otherwise()
            // Throw an exception when the catalog item type is not supported
            // for processing.
            .throwException(new Exception("Invalid catalog item type"));
   }

   @Bean
   public ServletRegistrationBean camelServletRegistrationBean() {
      ServletRegistrationBean registration = new ServletRegistrationBean(
            new CamelHttpTransportServlet(), "/api/*");
      registration.setName("CamelServlet");
      return registration;
   }

   @Bean
   public ServletRegistrationBean swaggerServletRegistrationBean() {
      ServletRegistrationBean registration = new ServletRegistrationBean(
            new RestSwaggerServlet(), "/api-docs/*");
      registration.setName("SwaggerServlet");
      return registration;
   }

   @Bean
   public FilterRegistrationBean filterRegistrationBean() {
      FilterRegistrationBean filterBean = new FilterRegistrationBean();
      filterBean.setFilter(new RestSwaggerCorsFilter());
      List<String> urlPatterns = new ArrayList<String>();
      urlPatterns.add("/camel-order-management-service/api-docs/*");
      urlPatterns.add("/camel-order-management-service/api/*");
      filterBean.setUrlPatterns(urlPatterns);
      return filterBean;
   }

}
