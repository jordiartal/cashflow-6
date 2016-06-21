mvnw package -Pprod -DskipTests
pause
heroku deploy:jar --jar target/*.war

