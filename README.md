# BootRestJwt
Spring Boot Rest with Spring Security and JSON Web Tokens (JWT) based Authentication using Mysql  

# Reference
https://github.com/brahalla/Cerberus

# Environment
Spring Boot 1.4.0 , Spring Security 4.0.3<br>
Maven<br>
mysql , mybatis 3.3.1 , mybatis-spring 1.2.4<br>
using @RestController No jerysy<br>
jjwt 0.6.0<br>
Logback<br>
External Tomcat 8 with https<br>

Usage
Request Authentication
```
curl -k -i -H "Content-Type: application/json" -X POST -d "{\"username\":\"admin\",\"password\":\"password\"}" https://localhost:8443/BootRestJwt/auth
```
Response
```
{
  "token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaWNvbXMiLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE0NTc3OTgxNzg3MzIsImV4cCI6MTQ1ODQwMjk3OH0.i7afS7aY5g-9MUkCzzaQeseDuBi9CqqE2PYFK42OwlOh0iu8asqsjaQPNkQYYWFsirNXxH9rRkQjAnYbCw0qvQ"
}
```
Request Service
```
curl  -k  -i -H "Content-Type: application/json" -H "X-Auth-Token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaWNvbXMiLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE0NTc3OTgxNzg3MzIsImV4cCI6MTQ1ODQwMjk3OH0.i7afS7aY5g-9MUkCzzaQeseDuBi9CqqE2PYFK42OwlOh0iu8asqsjaQPNkQYYWFsirNXxH9rRkQjAnYbCw0qvQ" -X GET https://localhost:8443/BootRestJwt/api/users
```
Token expiration : 1 week (mobile device always refresh)

# Tomcat Datasource JNDI
```
<GlobalNamingResources>
<Resource auth="Container" driverClassName="com.mysql.jdbc.Driver" 
loginTimeout="10" maxActive="200" maxIdle="8" maxWait="5000" 
name="jdbc/sim" username="dbuser" password="1234" 
type="javax.sql.DataSource"
url="jdbc:mysql://db.example.com:3306/exampledb?zeroDateTimeBehavior=convertToNull"/>      
</GlobalNamingResources>

<Context docBase="BootRestJwt" path="/BootRestJwt" reloadable="true">
<ResourceLink global="jdbc/sim" name="jdbc/sim" type="javax.sql.DataSource"/>
</Context>
```

# Tomcat SSL
```
    <Connector SSLEnabled="true" clientAuth="false" 
    keystoreFile="C:/job/sts/Servers/Tomcat v8.0 Server at localhost-config/.keystore" 
    keystorePass="1234" keystoreType="pkcs12" maxThreads="150" port="8443"
    protocol="org.apache.coyote.http11.Http11Protocol" scheme="https" secure="true" sslProtocol="TLS"/>
```
