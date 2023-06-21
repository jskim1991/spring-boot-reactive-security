Construct your `src/main/resources/application.yml`.
Sample looks like this:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

jwt:
  secret: "" # REPLACE_WITH_YOUR_SECRET_KEY

naver:
  grant-type: "authorization_code"
  response-type: "code"
  client-id: "" # REPLACE_WITH_YOUR_CLIENT_ID
  client-secret: "" # REPLACE_WITH_YOUR_CLIENT_SECRET
  redirect-uri: "http://localhost:3000/redirect" # REPLACE_WITH_YOUR_REDIRECT_URI
```