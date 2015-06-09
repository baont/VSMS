package vn.me.ui;

import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import vn.me.ui.common.T;
import vn.me.ui.interfaces.IActionListener;

/**
 *
 * @author TamDinh
 */
public class SMSComposer extends Form implements CommandListener {

    public static String defaultMessage;
    private TextField toWhom;
    private TextField message;
    private Command send, exit;
    private Display display;
    private IActionListener exitAction;

    public SMSComposer(String title, String defaultMessage, IActionListener exitAction, Display currentDisplay) {
        super(title);
        toWhom = new TextField(T.gL(14) + ":", "", 15, TextField.PHONENUMBER);
        message = new TextField(T.gL(10) + ":", defaultMessage, 600, TextField.ANY);
        send = new Command(T.gL(9), Command.OK,0);
        exit = new Command(T.gL(8), Command.EXIT, 1);
        this.exitAction = exitAction;
        append(toWhom);
        append(message);
        addCommand(send);
        addCommand(exit);
        this.display = currentDisplay;
        this.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        Alert alert;
        MessageConnection clientConn = null;
        if (c == exit){
            exitAction.actionPerformed( new Object[]{ new vn.me.ui.model.Command(-1, "",exitAction) , this } );
        }
        else if (c == send) {
            String mno = toWhom.getString();
            String msg = message.getString();
            if (mno.equals("")) {
                alert = new Alert(T.gL(12));
                alert.setString(T.gL(15));
                alert.setTimeout(2000);
                display.setCurrent(alert);
            } else {
                try {
                    clientConn = (MessageConnection) Connector.open("sms://" + mno);
                } catch (Exception e) {
                    alert = new Alert("Alert");
                    alert.setString(T.gL(11));
                    alert.setTimeout(2000);
                    display.setCurrent(alert);
                }
                try {
                    TextMessage textmessage = (TextMessage) clientConn.newMessage(MessageConnection.TEXT_MESSAGE);
                    textmessage.setAddress("sms://" + mno);
                    textmessage.setPayloadText(msg);
                    clientConn.send(textmessage);
                } catch (Exception e) {
                    alert = new Alert(T.gL(12), "", null, AlertType.INFO);
                    alert.setTimeout(Alert.FOREVER);
                    alert.setString(T.gL(13));
                    display.setCurrent(alert);
                }
            }
        }
    }
}
