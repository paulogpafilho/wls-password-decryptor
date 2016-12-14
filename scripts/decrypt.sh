#!/bin/bash
# Author: Paulo Albuquerque (paulogpafilho@gmail.com)
# 
# This script calls the WLSPasswordDecryptor class.
# The WLSPasswordDecryptor class decrypts Weblogic passwords present in
# config.xml, boot.properties and jdbc config files under DOMAIN_HOME/config/jdbc
# It requires two arguments:
# Argument 1: the full path to the domain home
# Argument 2: the full path to the Weblogic home
# Example decrypt.cmd /u02/oracle/domains/base_domain /u01/oracle/Middleware/wlserver_10.3
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
function usage() {
    printf "Missing required parameters\n"
    printf "Usage: $0 DOMAIN_HOME WL_HOME\n"
}

if [ "$#" -eq "2" ]; then
    export CLASSPATH=$2/server/lib/weblogic_sp.jar:$2/server/lib/weblogic.jar:$2/../modules/features/weblogic.server.modules_10.3.6.0.jar:$DIR
    java WLSPasswordDecryptor $1
else
    usage
fi
