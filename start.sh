java -jar eureka-service/target/eureka-service.jar > logs/eureka-service.log 2>&1 &
java -jar zuul-service/target/zuul-service.jar > logs/zuul-service.log 2>&1 &
java -jar PayPal/paypal/target/paypal.jar > logs/paypal-service.log 2>&1 &
java -jar bitcoin/bitcoin/target/bitcoin.jar > logs/bitcoin-service.log 2>&1 &
java -jar banka/bank-ms/target/bank-ms.jar > logs/bank-ms-service.log 2>&1 &
java -jar userAndPaymentInfo/target/userandpaymentinfo.jar > logs/userandpaymentinfo-service.log 2>&1 &
java -jar banka/bank/target/bank.jar > logs/bank.log 2>&1 &
java -jar naucna-centrala/webshop/target/webshop.jar > logs/webshop.log 2>&1 &
