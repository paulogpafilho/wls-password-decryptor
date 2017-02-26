# Weblogic Password Decryptor

Created by Paulo Albuquerque

Email: paulogpafilho@gmail.com

This is a simple Weblogic password decrytor.

Useful if you forgot a password for the admin user or any of the datasources or other domain password.

Usage:

- Compile the class
- Run the command:
```java
    java WLSPasswordDecryptor DOMAIN_HOME
```
   where DOMAIN_HOME is the full path to the domain which you want to decrypt its passwords.

The Java program will scan the domain and try to decrypt the passwords from config.xml, boot.properties (if present) and all datasource xml under /config/jdbc/.