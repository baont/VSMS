/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsms;

import vn.me.ui.Font;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.LAF;
import vn.me.ui.common.Resource;
import vn.me.ui.common.ResourceManager;
import vn.me.ui.interfaces.IActionListener;
import vn.me.ui.model.FrameImage;

/**
 * @author bao
 */
public class Midlet extends MIDlet {

    public static String platform;
    public static String providerCode;

    public void startApp() {
        Displayable d = Display.getDisplay(this).getCurrent();
        if (d == null) {
            if (BaseCanvas.instance == null) {
                BaseCanvas.createBaseCanvas(this).start();
                start();
            }
            startGameLoop();
        } else {
            resumeGameLoop();
        }
    }

    public void pauseApp() {
        pauseGameLoop();
    }

    public void destroyApp(boolean unconditional) {
        stopGameLoop();
    }

    private void startGameLoop() {
        BaseCanvas.instance.showGameCanvas();
    }

    private void stopGameLoop() {
        BaseCanvas.isRunning = false;
        if (BaseCanvas.instance != null) {
            BaseCanvas.instance.gameMidlet.notifyDestroyed();
        }
        BaseCanvas.instance = null;
    }

    private void resumeGameLoop() {
        BaseCanvas.isPause = false;
        BaseCanvas.instance.showGameCanvas();
    }

    private void pauseGameLoop() {
        BaseCanvas.isPause = true;
    }

    private void start() {
        //#if bavapen 
//#         LAF.LIGHT = 0xA9BCD0;
//#         LAF.DARK = 0x3A6898;
//#         LAF.DARKER = 0x00004F;
        //#endif

        Font.isEmotion = false;
        Resource.setFileTable(ResourceManager.MUI_RES_FILE);
        ResourceManager.loadDefaultFonts();
        ResourceManager.loadMUIResources();
        ResourceManager.slider = Resource.createImage(ResourceManager.SLIDER_PNG);
        ResourceManager.sliderThumb = Resource.createImage(ResourceManager.SLIDERTHUMB_PNG);
        ResourceManager.checkBox = Resource.createImage(ResourceManager.CHECKBOX_PNG);
        ResourceManager.waitCircle = new FrameImage(Resource.createImage(ResourceManager.WAITCIRCLE_PNG), 6);
        LAF.setLAFMode(LAF.MGO);

        String name = "AFUN";
        providerCode = "JAV";
        //#ifdef bavapen 
//#         name = "Bavapen";
//#             providerCode = "TCM";
        //#elifdef VUH
//#         providerCode = "VUH";
        //#endif
        String p = System.getProperty("microedition.platform");
//        p = "GT-S5233W/S5233WDXIH2";
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < p.length(); i++) {
            char ch = p.charAt(i);
            if (ch != ' ') {
                if (ch == '/') {
                    break;
                }
                buff.append(ch);
            }
        }
        platform = buff.toString();
        System.out.println(platform);

        new VSMSScreen(name, 0, null).switchToMe(0);
    }

    public static void sendSMS(final String data, final String address, final IActionListener successAction, final IActionListener failAction) {
        //#if DefaultConfiguration
	System.out.println("Clietn Send sms " + data + ":" + address);
        //#endif
//        final String address = to;
        new Thread(new Runnable() {

            MessageConnection smsconn = null;

            public void run() {
                try {
                    smsconn = (MessageConnection) Connector.open(address);
                    TextMessage txtmessage = (TextMessage) smsconn.newMessage(MessageConnection.TEXT_MESSAGE);
                    txtmessage.setAddress(address);
                    txtmessage.setPayloadText(data);
                    smsconn.send(txtmessage);
                    successAction.actionPerformed(new Object[]{null, "smsOK"});
                } catch (Exception e) {
                    failAction.actionPerformed(new Object[]{null, "smsFail"});
                } finally {
                    if (smsconn != null) {
                        try {
                            smsconn.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        }).start();
    }

}
