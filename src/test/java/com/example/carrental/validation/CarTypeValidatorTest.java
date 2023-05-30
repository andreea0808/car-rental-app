//package com.example.carrental.validation;
//
//import com.example.carrental.dto.CarDto;
//import com.example.carrental.model.CarType;
//import jakarta.validation.ClockProvider;
//import jakarta.validation.ConstraintValidatorContext;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@TestPropertySource(properties = {
//        "app.economyMax=18",
//        "app.standardMax=33",
//        "app.suvMin=33"
//})
//class CarTypeValidatorTest {
//
//    @Autowired
//    private CarTypeValidator carTypeValidator;
//
//    private ConstraintValidatorContext context;
//
//    @BeforeEach
//    public void setUp() {
//        context = new ConstraintValidatorContext() {
//            @Override
//            public void disableDefaultConstraintViolation() {
//            }
//
//            @Override
//            public String getDefaultConstraintMessageTemplate() {
//                return null;
//            }
//
//            @Override
//            public ClockProvider getClockProvider() {
//                return null;
//            }
//
//            @Override
//            public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate) {
//                return new ConstraintViolationBuilder() {
//                    @Override
//                    public NodeBuilderDefinedContext addNode(String name) {
//                        return null;
//                    }
//
//                    @Override
//                    public NodeBuilderCustomizableContext addPropertyNode(String name) {
//                        return null;
//                    }
//
//                    @Override
//                    public LeafNodeBuilderCustomizableContext addBeanNode() {
//                        return null;
//                    }
//
//                    @Override
//                    public ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String s, Class<?> aClass, Integer integer) {
//                        return null;
//                    }
//
//                    @Override
//                    public NodeBuilderDefinedContext addParameterNode(int i) {
//                        return null;
//                    }
//
//                    @Override
//                    public ConstraintValidatorContext addConstraintViolation() {
//                        return null;
//                    }
//                };
//            }
//
//            @Override
//            public <T> T unwrap(Class<T> type) {
//                return null;
//            }
//        };
//    }
//
//    @Test
//    void testValidEconomyCar() {
//        CarDto carDto = new CarDto(1L, "Honda", "Civic", "red", CarType.ECONOMY, BigDecimal.valueOf(10));
//        assertTrue(carTypeValidator.isValid(carDto, context));
//    }
//
//    @Test
//    void testInvalidEconomyCar() {
//        CarDto carDto = new CarDto(1L, "Honda", "Civic", "red", CarType.ECONOMY, BigDecimal.valueOf(20));
//        assertFalse(carTypeValidator.isValid(carDto, context));
//    }
//
//    @Test
//    void testValidStandardCar() {
//        CarDto carDto = new CarDto(1L, "Toyota", "Camry", "red", CarType.STANDARD, BigDecimal.valueOf(25));
//        assertTrue(carTypeValidator.isValid(carDto, context));
//    }
//
//    @Test
//    void testInvalidStandardCar_lowPrice() {
//        CarDto carDto = new CarDto(1L, "Toyota", "Camry", "red", CarType.STANDARD, BigDecimal.valueOf(10));
//        assertFalse(carTypeValidator.isValid(carDto, context));
//    }
//
//    @Test
//    void testInvalidStandardCar_highPrice() {
//        CarDto carDto = new CarDto(1L, "Toyota", "Camry", "red", CarType.STANDARD, BigDecimal.valueOf(40));
//        assertFalse(carTypeValidator.isValid(carDto, context));
//    }
//}