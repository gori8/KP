java -jar eureka-service/target/eureka-service.jar > logs/eureka-service.log 2>&1 &
java -jar zuul-service/target/zuul-service.jar > logs/zuul-service.log 2>&1 &
java -jar PayPal/paypal/target/paypal.jar > logs/paypal-service.log 2>&1 &
java -jar userAndPaymentInfo/target/userandpaymentinfo.jar > logs/userandpaymentinfo-service.log 2>&1 &

