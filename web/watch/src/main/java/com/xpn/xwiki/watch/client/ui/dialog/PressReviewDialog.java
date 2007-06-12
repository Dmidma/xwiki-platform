package com.xpn.xwiki.watch.client.ui.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.xpn.xwiki.gwt.api.client.app.XWikiGWTApp;
import com.xpn.xwiki.gwt.api.client.app.XWikiAsyncCallback;
import com.xpn.xwiki.watch.client.Watch;

/**
 * Copyright 2006,XpertNet SARL,and individual contributors as indicated
 * by the contributors.txt.
 * <p/>
 * This is free software;you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation;either version2.1of
 * the License,or(at your option)any later version.
 * <p/>
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software;if not,write to the Free
 * Software Foundation,Inc.,51 Franklin St,Fifth Floor,Boston,MA
 * 02110-1301 USA,or see the FSF site:http://www.fsf.org.
 *
 * @author ldubost
 */

public class PressReviewDialog extends WatchDialog {
    protected HTML pressReviewHTML;
    protected String pressReviewPage;

    /**
     * Choice dialog
     * @param app  XWiki GWT App object to access translations and css prefix names
     * @param name dialog name
     * @param buttonModes button modes WatchDialog.BUTTON_CANCEL|WatchDialog.BUTTON_NEXT for Cancel / Next
     */
    public PressReviewDialog(XWikiGWTApp app, String name, int buttonModes, String pressReviewPage) {
        super(app, name, buttonModes);
        this.pressReviewPage = pressReviewPage;

        FlowPanel main = new FlowPanel();
        main.addStyleName(getCSSName("main"));

        HTMLPanel invitationPanel = new HTMLPanel(app.getTranslation(getDialogTranslationName() + ".invitation"));
        invitationPanel.addStyleName(getCssPrefix() + "-invitation");
        main.add(invitationPanel);
        main.add(getAnalysisPanel());
        main.add(getActionsPanel());
        add(main);
    }

    protected Widget getAnalysisPanel() {
        pressReviewHTML = new HTML();
        Watch watch = (Watch) app;
        watch.getDataManager().getPressReview(watch.getFilterStatus(), pressReviewPage, new XWikiAsyncCallback(watch) {
            public void onSuccess(Object result) {
                super.onSuccess(result);
                pressReviewHTML.setHTML((String) result);
            }
        });
        pressReviewHTML.setStyleName(getCssPrefix() + "-html");
        return pressReviewHTML;
    }

}
