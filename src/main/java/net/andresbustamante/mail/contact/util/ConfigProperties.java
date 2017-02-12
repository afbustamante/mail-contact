package net.andresbustamante.mail.contact.util;

import java.util.ResourceBundle;

/**
 * @author andresbustamante
 */
public class ConfigProperties {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

    /**
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return bundle.getString(key);
    }
}
