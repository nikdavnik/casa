/*
 * cred-manager is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2017, Gluu
 */
package org.gluu.casa.ui.vm.user;

import org.gluu.casa.ui.UIUtils;
import org.gluu.casa.misc.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.cdi.DelegatingVariableResolver;

/**
 * Created by jgomer on 2018-07-09.
 */
@VariableResolver(DelegatingVariableResolver.class)
public class PassResetViewModel extends UserViewModel {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirm;
    private int strength;

    @DependsOn("strength")
    public String getStrengthText() {
        String str = null;
        if (strength >= 0) {
            str = Labels.getLabel("usr.pass.strength.level." + strength);
        }
        return str;
    }

    public int getStrength() {
        return strength;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    @Init(superclass = true)
    public void childInit() {
        strength = -1;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireEventListeners(view, this);
    }

    @Listen("onData=#new_pass")
    public void notified(Event event) throws Exception {
        if (Utils.isNotEmpty(newPassword)) {
            strength = (int) event.getData();
        } else {
            strength = -1;
        }
        BindUtils.postNotifyChange(null, null, this, "strength");
    }

    @NotifyChange("*")
    @Command
    public void resetPass() {

        if (userService.passwordMatch(user.getUserName(), currentPassword)) {
            if (newPasswordConfirm != null && newPasswordConfirm.equals(newPassword)) {

                if (userService.changePassword(user.getId(), newPassword)) {
                    logger.info(Labels.getLabel("app.pass_resetted"), user.getUserName());
                    resetPassSettings();
                    UIUtils.showMessageUI(true, Labels.getLabel("usr.passreset_changed"));
                } else {
                    UIUtils.showMessageUI(false);
                }

            } else {
                UIUtils.showMessageUI(false, Labels.getLabel("usr.passreset_nomatch"));
                newPasswordConfirm = null;
                newPassword = null;
                strength = -1;
            }
        } else {
            currentPassword = null;
            UIUtils.showMessageUI(false, Labels.getLabel("usr.passreset_badoldpass"));
        }

    }

    public void resetPassSettings() {
        newPassword = null;
        newPasswordConfirm = null;
        currentPassword = null;
        strength = -1;
    }

    @NotifyChange("*")
    @Command
    public void cancel() {
        resetPassSettings();
    }

}
