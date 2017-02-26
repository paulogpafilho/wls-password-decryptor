# Weblogic Password Decryptor

Created by Paulo Albuquerque

Email: paulogpafilho@gmail.com

This is a simple Weblogic password decrytor.

Useful if you forgot a password for the admin user or any of the datasources or other domain password.

Usage:

- Compile the class (weblogic.jar or wlfullclient.jar required in the classpath for compilation)
- Copy the script decrypt.(sh/cmd) to the same location as the java .class file
- Run the command:

```bash
   decrypt.sh DOMAIN_HOME WLS_HOME
```
   where:
   - DOMAIN_HOME is the full path to the domain which you want to decrypt its passwords.
   - WLS_HOME is the full path to the Weblogic Binaries for the domain which you want to decrypt its passwords.
   
   example:
   
```bash
   decrypt.cmd /u02/oracle/domains/base_domain /u01/oracle/Middleware/wlserver_10.3
```

The Java program will scan the domain and try to decrypt the passwords from config.xml, boot.properties (if present) and all datasource xml under /config/jdbc/.