/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class AccessControlUtil {
    public static boolean hasPermission(HttpServletRequest request, String pageCode) {
        Map<String, Boolean> grantedPages = (Map<String, Boolean>) request.getSession().getAttribute("grantedPages");
        if (grantedPages == null) return false;
        return grantedPages.getOrDefault(pageCode, false);
    }
}
