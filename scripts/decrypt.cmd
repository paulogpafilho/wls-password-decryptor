REM Author: Paulo Albuquerque (paulogpafilho@gmail.com)
REM 
REM This script calls the WLSPasswordDecryptor class.
REM The WLSPasswordDecryptor class decrypts Weblogic passwords present in
REM config.xml, boot.properties and jdbc config files under DOMAIN_HOME/config/jdbc
REM It requires two arguments:
REM Argument 1: the full path to the domain home
REM Argument 2: the full path to the Weblogic home
REM Example decrypt.cmd /u02/oracle/domains/base_domain /u01/oracle/Middleware/wlserver_10.3
@ECHO  OFF
IF [%1]==[] goto usage
IF [%2]==[] goto usage

:execution
	set CLASSPATH=%2\server\lib\weblogic_sp.jar;%2\server\lib\weblogic.jar;%2\..\modules\features\weblogic.server.modules_10.3.6.0.jar;%cd%
	java WLSPasswordDecryptor %1
	goto :eof

:usage
    @echo Missing required parameters
	@echo Usage: %0 DOMAIN_HOME WL_HOME
	goto :eof
