package vn.me.ui;

import vn.me.ui.model.Command;
import vn.me.core.BaseCanvas;
//#if !iwin_lite
import vn.me.ui.common.Effects;
//#endif
import vn.me.ui.common.ResourceManager;
import vn.me.ui.common.LAF;

/**
 * @author
 *
 */
public class InputDialog extends Dialog {

    protected String[] info;
    public EditField txtInput;
    public EditField txtArrInput[];
    public Label lblLabel[];
    public Label lblTitle;
    private int nGroup = 0;
    private boolean type = false;

    public InputDialog(String text, Command ok, Command back, int inputType) {
        //#if BigScreen
//#         super(8, BaseCanvas.h - LAF.LOT_ITEM_HEIGHT - 69 - LAF.LOT_PADDING, BaseCanvas.w - 16 > 240 ? 240 : BaseCanvas.w - 16, 69);
        //#else
        super(8, BaseCanvas.h - LAF.LOT_ITEM_HEIGHT - 69 - LAF.LOT_PADDING, BaseCanvas.w - 16, 69);
        //#endif
        padding = LAF.LOT_PADDING;

        type = true;
        int editFH = ResourceManager.biFontBlack.getHeight() + 12;
        txtInput = new EditField();
        txtInput.setMetrics(0, h - LAF.LOT_ITEM_HEIGHT - 2 * (padding + border), w - 2 * (padding + border), editFH);
        addWidget(txtInput);
        cmdCenter = ok;
        cmdLeft = back;
        setInfo(text, inputType);
        setPosition(x, BaseCanvas.hh - h / 2);
        txtInput.setPosition(0, h - txtInput.h - (padding + border));
        
        defaultFocusWidget = txtInput;
        //#if BigScreen
//#         addControlButtons();
        //#endif
    }

    //#if BigScreen
//#     private void addControlButtons() {
//#         try {
//#             spacing = 10;
//#             int btnH = ResourceManager.imgButtonBg.getHeight();
//#             int btnW = w - ((border + padding) << 1);
//#             Button btnRight = null, btnLeft = null;
//#             int count = 0;
//#             if(cmdLeft != null && !cmdLeft.caption.equals("")) {
//#                count++;
//#                btnRight = new Button(cmdLeft.caption);
//#                btnRight.align = Font.CENTER;
//#             }
//#             Command cmd = cmdCenter;
//#             if (cmd != null && !cmd.caption.equals("")) {
//#                 count++;
//#                 btnLeft = new Button(cmd.caption);
//#                 btnLeft.align = Font.CENTER;
//#             }
//#             if (count > 0) {
//#                 h += btnH + 3;
//#                 if (count == 2) {
//#                     btnW = (w - ((border + padding) << 1) - spacing) >> 1;
//#                 }
//#             }
//#             if (btnRight != null) {
//#                 btnRight.setMetrics(count == 1 ? 0 : w - ((border + padding) << 1) - btnW,
//#                         h - ((border + padding) << 1) - btnH, btnW, btnH);
//#                 btnRight.cmdCenter = cmdLeft;
//#                 btnRight.type = 6;
//#                 addWidget(btnRight);
//#             }
//#             if (btnLeft != null) {
//#                 btnLeft.setMetrics(0,
//#                         h - ((border + padding) << 1) - btnH, btnW, btnH);
//#                 btnLeft.cmdCenter = cmd;
//#                 btnLeft.type = 6;
//#                 addWidget(btnLeft);
//#             }
//#             x = destX = (BaseCanvas.w - w) >> 1;
//#             y = destY = (BaseCanvas.h - h) >> 1;
//#         } catch (Exception e) { 
//# //            e.printStackTrace(); 
//#         }
//#     }
    //#endif
    public String getText(int index) {
        if (txtInput != null) {
            return txtInput.getText();
        }
        return txtArrInput[index].getText();
    }

    public InputDialog(String title, String[] labels, int[] inputType, Command ok, Command back) {
        this(title, labels, inputType, null, ok, back);
    }

    public InputDialog(String title, String[] labels, int[] inputType, int[] inputLengths, Command ok, Command back) {
        //#if BIG_SCREEN
//# 	super(
//# 		BaseCanvas.w / 10,
//# 		100,
//# 		4 * BaseCanvas.w / 5,
//# 		2 * LAF.LOT_ITEM_HEIGHT * labels.length + LAF.LOT_PADDING + LAF.LOT_TITLE_HEIGHT);
        //#else
        super(
                BaseCanvas.w / 10,
                100,
                4 * BaseCanvas.w / 5,
                2 * LAF.LOT_ITEM_HEIGHT * labels.length + LAF.LOT_PADDING + LAF.LOT_TITLE_HEIGHT);
        //#endif
        padding = LAF.LOT_PADDING;
        nGroup = inputType.length;
        txtArrInput = new EditField[nGroup];
        lblLabel = new Label[nGroup];
        type = false;
        isLoop = true;
        spacing = 2;
        if (title != null) {
            lblTitle = new Label(title, ResourceManager.bigFont);
            lblTitle.padding = 0;
            lblTitle.setMetrics(0, 0, w, lblTitle.normalfont.getHeight() + 2);//LAF.LOT_ITEM_HEIGHT);
            lblTitle.align = Font.CENTER;
            addWidget(lblTitle);

        }

        for (int i = 0; i < nGroup; i++) {
            txtArrInput[i] = new EditField(0, LAF.LOT_ITEM_HEIGHT + padding, w - ((padding + border) << 1),
                    LAF.LOT_ITEM_HEIGHT);
            txtArrInput[i].inputType = inputType[i];
            if (inputLengths != null) {
                txtArrInput[i].maxTextLenght = inputLengths[i];
            }
//	    txtArrInput[i].boldFont = ResourceManager.whiteFont;
            lblLabel[i] = new Label(labels[i]);
            lblLabel[i].padding = 0;
            lblLabel[i].setMetrics(0, (padding << 1), w - (padding << 1), lblLabel[i].normalfont.getHeight() + 2);
            addWidget(lblLabel[i]);
            addWidget(txtArrInput[i]);
        }
        cmdCenter = ok;
        cmdLeft = back;

//        isAutoFit = true;
        setViewMode(WidgetGroup.VIEW_MODE_LIST);
        defaultFocusWidget = txtArrInput[0];
        if (preferredSize.height < BaseCanvas.h - LAF.LOT_TITLE_HEIGHT - LAF.LOT_CMDBAR_HEIGHT) {
            this.h = preferredSize.height + ((border + padding) << 1);
        } else {
            lblTitle.isFocusable = true;
            this.h = BaseCanvas.h - LAF.LOT_TITLE_HEIGHT - LAF.LOT_CMDBAR_HEIGHT - 10;
        }
        this.destY = (BaseCanvas.h - this.h) / 2;
        if (preferredSize.height > h) {
            isScrollableY = true;
        }
        //#if BigScreen
//#         addControlButtons();
        //#endif
    }

    public void setTitle(String info) {
        this.info = ResourceManager.boldFont.wrap(info, w - 2 * (padding + border));
        preferredSize.height = this.info.length * ResourceManager.boldFont.getHeight() + txtInput.h + 2 * (padding + border);
        if (preferredSize.height >= 70) {
            h = preferredSize.height;
        }
    }

    private void setInfo(String info, int inputType) {

//        txtInput.inputType = inputType;
        txtInput.setInputType(inputType);
        txtInput.setText("");
        this.info = ResourceManager.bigFont.wrap(info, w - 2 * (padding + border));
        preferredSize.height = this.info.length * ResourceManager.bigFont.getHeight() + txtInput.h + 2 * (padding + border);
        if (preferredSize.height >= 70) {
            h = preferredSize.height;
        }

    }

    public void paint() {
        super.paint();
        if (!type) {
            //#if !iwin_lite
            if (LAF.mode == LAF.IWIN) {
                Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_BACKGROUND_DARKER, 0xffffffff, this.padding, -this.scrollY + LAF.LOT_ITEM_HEIGHT - 6, (this.w >> 1) - this.padding, 1, true);
                Effects.drawLinearGradient(BaseCanvas.g, 0xffffffff, LAF.CLR_BACKGROUND_DARKER, (this.w >> 1) - this.padding, -this.scrollY + LAF.LOT_ITEM_HEIGHT - 6, (this.w >> 1) - this.padding, 1, true);
                //   g.drawLine( 0, -this.scrollY  + LAF.LOT_ITEM_HEIGHT >> 1 , BaseCanvas.w, -this.scrollY + LAF.LOT_ITEM_HEIGHT >> 1 );
            } else {//mGO
                Effects.drawLinearGradient(BaseCanvas.g, 0x4b779a, 0xffffffff, this.padding, -this.scrollY + LAF.LOT_ITEM_HEIGHT - 6, (this.w >> 1) - this.padding, 1, true);
                Effects.drawLinearGradient(BaseCanvas.g, 0xffffffff, 0x4b779a, (this.w >> 1) - this.padding, -this.scrollY + LAF.LOT_ITEM_HEIGHT - 6, (this.w >> 1) - this.padding, 1, true);
            }
            //#endif
            return;
        }

        int yStart = (txtInput.y >> 1) - ((info.length * ResourceManager.bigFont.getHeight()) >> 1);
        for (int i = 0, yLine = yStart; i < info.length; i++, yLine += ResourceManager.bigFont.getHeight()) {
            ResourceManager.bigFont.drawString(BaseCanvas.g, info[i], (w >> 1) - padding - border, yLine, Font.CENTER);
        }
    }
}
