java -jar eureka-service/target/eureka-service.jar > logs/eureka-service.log 2>&1 &
java -jar zuul-service/target/zuul-service.jar > logs/zuul-service.log 2>&1 &
java -jar banka/bank-ms/target/bank-ms.jar > logs/bank-ms-service.log 2>&1 &
java -jar userAndPaymentInfo/target/userandpaymentinfo.jar > logs/userandpaymentinfo-service.log 2>&1 &
java -jar banka/bank/target/bank.jar > logs/bank.log 2>&1 &
java -jar nc-demo/target/nc-demo.jar > logs/nc-demo.log 2>&1 &
java -jar pcc/target/pcc.jar > logs/pcc.log 2>&1 &
java -jar banka/red-bank/target/red-bank.jar > logs/red-bank.log 2>&1 &
