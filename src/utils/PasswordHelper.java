package utils;

import java.security.MessageDigest;

public class PasswordHelper {
	public static String toMD5(String text) {
        String output="";
       try {
           byte[] textBytes = text.getBytes("UTF-8");
           MessageDigest md = MessageDigest.getInstance("MD5");
           md.update(textBytes);
           byte[] code = md.digest();
           output = new String(code);
       } catch (Exception ex) {}
       return output;
   }
}
