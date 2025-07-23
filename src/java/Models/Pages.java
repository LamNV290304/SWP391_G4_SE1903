/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Admin
 */
public class Pages {
    private String pageCode;
    private String pagePath;
    private String displayName;
    private String iconClass;

    public Pages() {
    }

    
    public Pages(String pageCode, String pagePath, String displayName, String iconClass) {
        this.pageCode = pageCode;
        this.pagePath = pagePath;
        this.displayName = displayName;
        this.iconClass = iconClass;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    
}
