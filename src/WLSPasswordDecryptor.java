import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

/**
 * Decrypts WLS passwords from config.xml, boot.properties and jdbc datasource
 * files. Requires weblogic_sp.jar, weblogic.jar and
 * weblogic.server.modules_10.3.6.0.jar in the CLASSPATH
 * 
 * @author Paulo Albuquerque
 *
 */
public class WLSPasswordDecryptor {

    // Encryption/Decryption service
    private ClearOrEncryptedService ces;
    // domain full path
    private String domain_path;

    /**
     * The main method receives one argument: the full path to the domain which
     * passwords will be decrypted
     * 
     * @param args
     *            the full path to the domain
     */
    public static void main(String[] args) {
        // check if there is at least one arguments passed to the main method
        if (args == null || args.length < 1) {
            System.out.println("Missing domain full path.");
            System.out.println("Usage: java WLSPasswordDecryptor <DOMAIN_HOME>");
            System.out.println("For example: java WLSPasswordDecryptor /u02/oracle/domains/base_domain");
        } else {
            WLSPasswordDecryptor d = new WLSPasswordDecryptor();
            d.decryptPasswords(args[0]);
        }
    }

    /**
     * Decrypts all available passwords from config.xml, boot.properties and
     * jdbc datasource files.
     * 
     * @param path
     *            the full path to the domain
     */
    private void decryptPasswords(String path) {
        try {
            // checks if the path is a directory and can be read
            File f = new File(path);
            if (f.exists() && f.isDirectory() && f.canRead()) {
                domain_path = path;
                // obtains a instance of the wLS encryption/decryption service
                // throws NoClassDefFoundError if the required jars are not on
                // the classpath
                ces = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService(domain_path + "/security"));
                // calls each of the methods to decrypt passwords from the
                // different files
                System.out.println("-------------- WLS Password Decryptor --------------\n\n");
                decryptConfigXMLPasswords();
                getBootPropertiesPasswords();
                getJDBCPasswords();
            } else {
                System.out.println("Domain path does not exist or cannot be read.");
                System.out.println("Provide the full path to the domain, ie: /u02/oracle/domains/base_domain");
            }
        } catch (NoClassDefFoundError ncdf) {
            System.out.println("Unable to load WLS core classes.");
            System.out.println(
                    "Set CLASSPATH to include weblogic_sp.jar, weblogic.jar and weblogic.server.modules_10.3.6.0.jar");
        }
    }

    /**
     * Decrypt the config.xml passwords
     */
    private void decryptConfigXMLPasswords() {
        // the config.xml file
        File f = new File(domain_path + "/config/config.xml");
        // check if the file exists and can be read
        if (f.exists() && f.isFile() && f.canRead()) {
            String line = null;
            BufferedReader br;
            try {
                // reads each file line
                br = new BufferedReader(new FileReader(f));
                System.out.println("----- config.xml passwords -----");
                while ((line = br.readLine()) != null) {
                    // if a line contains the ecrypted password prefix, we
                    // extract the encrypted password and add to the collection
                    if (line.contains("{AES}")) {
                        String pw = line.substring(line.indexOf("{AES}"), line.indexOf("<", line.indexOf("{AES}")));
                        System.out.println(pw + " --> " + ces.decrypt(pw));
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("An error happened while reading the config.xml file");
                e.printStackTrace();
            }
        }
    }

    /**
     * Decrypt the boot.properties passwords
     */
    private void getBootPropertiesPasswords() {
        // the boot.properties file
        File f = new File(domain_path + "/servers/AdminServer/security/boot.properties");
        // check if the file exists and can be read
        if (f.exists() && f.isFile() && f.canRead()) {
            String line = null;
            try {
                // read each file line
                BufferedReader br = new BufferedReader(new FileReader(f));
                System.out.println("----- boot.properties passwords -----");
                while ((line = br.readLine()) != null) {
                    // if a line contains the ecrypted password prefix, we
                    // extract the encrypted password and add to the collection
                    if (line.contains("{AES}")) {
                        String pw = line.split("=", 2)[1];
                        System.out.println(pw + " --> " + ces.decrypt(pw));
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("An error happened while reading the boot.properties file");
                e.printStackTrace();
            }
        }
    }

    /**
     * Decrypt all the jdbc config files found under config/jdbc folder
     */
    private void getJDBCPasswords() {
        // the jdbc folder where all the jdbc config files reside
        File jdbc_dir = new File(domain_path + "/config/jdbc");
        // check if the jdbc directory exits and can be read
        if (jdbc_dir.exists() && jdbc_dir.isDirectory() && jdbc_dir.canRead()) {
            // get a list of the files in the directory
            File[] listOfFiles = jdbc_dir.listFiles();
            // iterate the list of files
            for (int i = 0; i < listOfFiles.length; i++) {
                // check if the file can be read and is a xml config file
                if (listOfFiles[i].isFile() && listOfFiles[i].canRead() && listOfFiles[i].getName().endsWith(".xml")) {

                    String line;
                    try {
                        System.out.println("----- " + listOfFiles[i].getName() + " passwords -----");
                        // read each file line
                        BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i]));
                        while ((line = br.readLine()) != null) {
                            // if a line contains the ecrypted password prefix,
                            // we extract the encrypted password and add to the
                            // collection
                            if (line.contains("{AES}")) {
                                String pw = line.substring(line.indexOf("{AES}"),
                                        line.indexOf("<", line.indexOf("{AES}")));
                                System.out.println(pw + " --> " + ces.decrypt(pw));
                            }
                        }
                        br.close();
                    } catch (IOException e) {
                        System.out.println("An error happened while reading the jdbc file");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
