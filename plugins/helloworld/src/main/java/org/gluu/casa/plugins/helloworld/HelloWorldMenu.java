/*
 * cred-manager is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2018, Gluu
 */
package org.gluu.casa.plugins.helloworld;

import org.gluu.casa.extension.navigation.MenuType;
import org.gluu.casa.extension.navigation.NavigationMenu;
import org.pf4j.Extension;

/**
 * An extension class implementing the {@link NavigationMenu} extension point.
 * @author jgomer
 */
@Extension
public class HelloWorldMenu implements NavigationMenu {

    public String getContentsUrl() {
        return "menu.zul";
    }

    public MenuType menuType() {
        return MenuType.USER;
    }

    public float getPriority() {
        return 0.5f;
    }

}
