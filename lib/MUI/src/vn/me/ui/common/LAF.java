/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.me.ui.common;

import vn.me.core.BaseCanvas;
import vn.me.ui.*;
import vn.me.ui.model.Command;

/**
 *
 * @author winner Class chứa style layout (màu sắc, Kích thước chuẩn của một số
 * control, ect). Class chứa các hàm vẽ dialog border, nền của dialog,
 * editfield, ect... Mục đích: Khi các UI vẽ thì nó se goi cac ham trong class
 * nay de ve. Khi thay doi style thi chỉ thay doi trong file nay ma thoi.
 */
public class LAF {
    // KHU VUC MAU SAC
    public static int LIGHT = 0x9DD149;
    public static int DARK = 0x62A300;
    public static int DARKER = 0x3D5513;
    public static int CLR_BACKGROUND_DARKER = 0x05456b;// 0x202025;//0x222222;//0x333333;//0x333333;//250400;
    public static int CLR_BACKGROUND_LIGHTER = 0x0869a0;//0xac0a18;//0x888888;//0x550000;//0x666666;//250400;
    public static int CLR_BORDER = 0xffffff;//0x736D6B;//0xd9e1f1;
    
    public static int CLR_BORDER_FOCUSED = 0x736D6B;//0x008CFF;//0xd9e1f1;
   
    public static int CLR_BORDER_PRESSED = 0xdd1111;
//    public static int CLR_DIALOG_BGR = 0x660000;
    public static final int CLR_MENU_BGR = 0x05456b;//0x222830;//
//    public static int CLR_MENU_BGR = 0xffffff;//0x222830;//
    public static int CLR_SELECTED_ITEM_DARKER = 0xc323232;//0x3079ed;//0xc323232;0xc0c0c0;//
    public static int CLR_SELECTED_ITEM_LIGHTER = 0x606060;//0x4787ed;//0x606060;// 0xcc0033;0x808080;//
    public static int CLR_POP_BGR_DARKER = 0x330000;
    public static int CLR_POP_BGR_LIGHTER = 0x633333;// 0xcc0033;
    public static int CLR_ERROR_LIGHTER = 0xff0000;// 0xcc0033;
    public static int CLR_ERROR_DARKER = 0xCC0000;
    public static int CLR_MENU_BAR_LIGHTER = 0x666666;
    public static int CLR_MENU_BAR_DARKER = 0x222222;
    public static int CLR_TITLE_LIGHTER = 0x3079ed;//0xa40408;//0x660000;
    public static int CLR_TITLE_DARKER = 0x4787ed;//0x6c1b1d;//0x330000;
    public static int CLR_BANNER_BG = 0x0c3e88;
    public static int CLR_MY_AVATAR = 0xee3333;
    public static int CLR_WHITE_BGR = 0xffff99;
    public static int CLR_EDITFIELD_BG = 0x000000;
    public static int CLR_EDITFIELD_EDIT_TYPE_BG = 0x52130d;
    public static int CLR_EDITFIELD_CARRET_COLROR = 0xffffff;
    
    public static int CLR_PRESSED_ITEM_DARKER = 0xff9000;//0xc323232;// 0xff9000;
    public static int CLR_PRESSED_ITEM_LIGHTER = 0xfac200;//0x606060;//0xfac200;// 0xcc0033;
    //#if Android || BigScreen
//#     public static int CLR_EDITFIELD_BACKGROUND = 0xD6CFCE;
//#     public static int CLR_EDITFIELD_FOREGROUND = 0xDE8252;
//#     public static int CLR_BUTTON_END = 0xD62821;
//#     public static int CLR_BUTTON_START =0x4A1010 ;
//#     public final static int LOT_IMAGE_ITEM_HEIGHT = 80;
//#     public static int LOT_TITLE_HEIGHT = 40;
//#     public static int LOT_ITEM_HEIGHT = 40;// = Font.boldFont.getHeight() + (LOT_PADDING << 1);
//#     public static int LOT_CMDBAR_HEIGHT = 40;
    //#else
    /** Kich thuoc image item.*/
    public static int LOT_IMAGE_ITEM_HEIGHT = 40;
    public static int LOT_TITLE_HEIGHT = 19 + 8;
    public static int LOT_ITEM_HEIGHT = 20;// = Font.boldFont.getHeight() + (LOT_PADDING << 1);
    public static int LOT_CMDBAR_HEIGHT = 19 + 8;
    //#endif
    public static int LOT_MARGIN = 8;
    /**
     * Khoang cach dem de ve noi dung ben trong mot control hay mot screen
     */
    public static int LOT_PADDING = 3;
    /**
     * Bien dieu khien viec bo goc.
     */
    //#if Android || BigScreen
//#     public static int LOT_ARC_SIZE = 32;
    //#else
    public static int LOT_ARC_SIZE = 6;
    //#endif
    
    /**
     * Bien dieu khien viec bo goc.
     */
    public static int LOT_CARD_ARC_SIZE = 8;
    /**
     * Kich thuoc avatar.
     */
    public final static int LOT_AVATAR_SIZE = 40;
    /**
     * Kich thuoc image ARROW.
     */
    public final static int LOT_ARROW_SIZE = 9;
//    /**
//     * Font dùng khi một item được selected.
//     */
    public static Font FONT_SELECTED_ITEM = ResourceManager.boldFont;

    public static byte mode;
    public static final byte IWIN = 0;
    public static final byte MGO = 1;
    //0 : iwin
    //1 : mgo
    public static void setLAFMode(byte type) {
        mode = type;
        switch (type) {
            case IWIN:
//                CLR_BORDER = 0xaaaaaa;//0xd9e1f1;
                CLR_BORDER_FOCUSED = 0xFFFFFF;//0xd9e1
                CLR_EDITFIELD_BG = 0x000000;
                CLR_EDITFIELD_EDIT_TYPE_BG = 0x52130d;
                CLR_EDITFIELD_CARRET_COLROR = 0xffffff;
                
                break;
            case MGO:
//                CLR_BORDER = 0x2e4b59;//0xd9e1f1;
                CLR_BORDER_FOCUSED = DARKER;//0xd9e1
                CLR_EDITFIELD_BG = 0xF1F1F1;
                CLR_EDITFIELD_EDIT_TYPE_BG = 0xF1F1F1;
                CLR_EDITFIELD_CARRET_COLROR = 0x000000;
                
                break;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Cac ham paint cho man hinh screen.">
    /**
     * Vẽ title cua man hinh.
     *
     * @param title
     */
    public static void paintScreenTitle(String title) {
        BaseCanvas.g.translate(-BaseCanvas.g.getTranslateX(), -BaseCanvas.g.getTranslateY());
        BaseCanvas.g.setClip(0, 0, BaseCanvas.w, LOT_TITLE_HEIGHT);
//        Effects.drawLinearGradient(BaseCanvas.g, CLR_BACKGROUND_DARKER, CLR_BACKGROUND_LIGHTER,
//                0, 0, BaseCanvas.w, LOT_TITLE_HEIGHT, false);
        BaseCanvas.g.setColor(DARK);
        BaseCanvas.g.fillRect(0, 0, BaseCanvas.w, LOT_TITLE_HEIGHT);
        BaseCanvas.g.setColor(LIGHT);
        BaseCanvas.g.fillRect(0, 0, BaseCanvas.w, 1);
        
        BaseCanvas.g.setColor(DARKER);
//        BaseCanvas.g.setColor(0);

        BaseCanvas.g.drawLine(0, LOT_TITLE_HEIGHT - 1, BaseCanvas.w, LOT_TITLE_HEIGHT - 1);

        //#if Android || BigScreen        
//#          Font.getDefaultFont().drawString(BaseCanvas.g, title, BaseCanvas.hw,
//#                  (LOT_TITLE_HEIGHT - ResourceManager.boldFont.getHeight()) >> 1, Font.CENTER, 0xffffff);
        //#else
        ResourceManager.bigFont.drawString(BaseCanvas.g, title, BaseCanvas.hw,
                (LOT_TITLE_HEIGHT - ResourceManager.bigFont.getHeight()) >> 1, Font.CENTER);
        //#endif
    }

    /**
     * Paint command bar cua mot screen.
     *
     * @param sc
     */
    public static void paintScreenCommandBar(Screen sc) {
        BaseCanvas.g.translate(-BaseCanvas.g.getTranslateX(), -BaseCanvas.g.getTranslateY());
        BaseCanvas.g.setClip(0, 0, BaseCanvas.w, BaseCanvas.h);
        // Ve command bar background
        //#if !BigScreen
//        if (Screen.imgSoft != null && !sc.transparentCommandBar) {// +1 o day de ko nhin thay chan la bai trong game
//            BaseCanvas.g.drawImage(Screen.imgSoft, 0, BaseCanvas.h - LOT_CMDBAR_HEIGHT + 1, BaseCanvas.TOPLEFT);
//        }
        BaseCanvas.g.setColor(LIGHT);
        BaseCanvas.g.fillRect(0, BaseCanvas.h - LOT_CMDBAR_HEIGHT + 1, BaseCanvas.w, 1);
        BaseCanvas.g.setColor(DARK);
        BaseCanvas.g.fillRect(0, BaseCanvas.h - LOT_CMDBAR_HEIGHT + 2, BaseCanvas.w, LOT_CMDBAR_HEIGHT);
        //#endif

        if (sc.currentMenu != null) {
            paintCommandBarText(sc.commandBarFont, null, sc.currentMenu.cmdCenter, sc.currentMenu.cmdRight);
        } else if (Screen.currentDialog != null) {
            //#if BigScreen
//#             // Neu la dialog thi chi ve command cua dialog thoi, khong quan tam den command trong man hinh.
//#             if (Screen.currentDialog instanceof MessageDialog || Screen.currentDialog instanceof InputDialog) {
//#                 return;
//#             }
//#             Widget wid = Screen.currentDialog.getFocusedWidget(true);
//#             if (wid != null) {
//#                 paintCommandBarText(wid.getLeftCommand(), wid.getCenterCommand(), wid.getRightCommand());
//#             } else {
//#                 paintCommandBarText(Screen.currentDialog.cmdLeft, Screen.currentDialog.cmdCenter, Screen.currentDialog.cmdRight);
//#             }
            //#else
            // Neu la dialog thi chi ve command cua dialog thoi, khong quan tam den command trong man hinh.
            Widget wid = Screen.currentDialog.getFocusedWidget(true);
            if (wid != null) {
                paintCommandBarText(sc.commandBarFont, wid.getLeftCommand(), wid.getCenterCommand(), wid.getRightCommand());
            } else {
                paintCommandBarText(sc.commandBarFont, Screen.currentDialog.cmdLeft, Screen.currentDialog.cmdCenter, Screen.currentDialog.cmdRight);
            }
            //#endif
        } else if (sc.getCurrentRoot() != null) {
            Widget wid = sc.getFocusedWidget(true);
            Command left = wid.getLeftCommand();
            Command center = wid.getCenterCommand();
            Command right = wid.getRightCommand();
            paintCommandBarText(sc.commandBarFont,
                    sc.currentMenu == null && left == null ? sc.cmdLeft : left,
                    sc.currentMenu == null && center == null ? sc.cmdCenter : center,
                    sc.currentMenu == null && right == null ? sc.cmdRight : right);
        } else {
            paintCommandBarText(sc.commandBarFont, sc.cmdLeft, sc.cmdCenter, sc.cmdRight);
        }
    }

    /**
     * *
     * Paint 3 command cho screen.
     *
     * @param left
     * @param center
     * @param right
     */
    public static void paintCommandBarText(Font font,Command left, Command center, Command right) {
        //<editor-fold defaultstate="collapsed" desc="Ve command left.">
        if (left != null) {
//            if (left.icon != null) {
//                BaseCanvas.g.drawImage(left.icon, LOT_PADDING,
//                        BaseCanvas.h - LOT_CMDBAR_HEIGHT + (LOT_PADDING >> 1), BaseCanvas.CENTERLEFT);
//            } else {
                font.drawString(BaseCanvas.g, left.caption, LOT_PADDING,
                        ((LOT_CMDBAR_HEIGHT - ResourceManager.boldFont.getHeight()) >> 1) + BaseCanvas.h - LOT_CMDBAR_HEIGHT, Font.LEFT);
//            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Ve command center.">
        if (center != null) {
//            if (center.icon != null) {
//                BaseCanvas.g.drawImage(center.icon, BaseCanvas.hw,
//                        BaseCanvas.h - LOT_CMDBAR_HEIGHT + (LAF.LOT_PADDING >> 1), BaseCanvas.CENTER);
//            } else {
                font.drawString(BaseCanvas.g, center.caption, BaseCanvas.hw,
                        ((LOT_CMDBAR_HEIGHT - ResourceManager.boldFont.getHeight()) >> 1) + BaseCanvas.h - LOT_CMDBAR_HEIGHT, Font.CENTER);
//            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Ve command right">
        if (right != null) {
//            if (right.icon != null) {
//                BaseCanvas.g.drawImage(right.icon, BaseCanvas.w - LOT_PADDING,
//                        BaseCanvas.h - LOT_CMDBAR_HEIGHT + (LAF.LOT_PADDING >> 1), BaseCanvas.TOPRIGHT);
//            } else {
                font.drawString(BaseCanvas.g, right.caption,
                        BaseCanvas.w - LOT_PADDING,
                        ((LOT_CMDBAR_HEIGHT - ResourceManager.boldFont.getHeight()) >> 1) + BaseCanvas.h - LOT_CMDBAR_HEIGHT, Font.RIGHT);
//            }
        }
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac ham paint cho dialog.">
    /**
     * Vẽ border tại vi tri tuy y
     *
     * @param x : int - Tọa độ x
     * @param y : int - Tọa độ y
     * @param w : int - Chiều dài
     * @param h : int - Chiều cao
     */
    public static void paintDialogBorder(int x, int y, int w, int h) {
        BaseCanvas.g.translate(x, y);
        BaseCanvas.g.setColor(DARKER);
        // top
        BaseCanvas.g.fillRect(2, 0, w - 4, 1);
        // bot
        BaseCanvas.g.fillRect(2, h - 1, w - 4, 1);
        // left
        BaseCanvas.g.fillRect(0, 2, 1, h - 4);
        // right
        BaseCanvas.g.fillRect(w - 1, 2, 1, h - 4);
        
        // top left
        BaseCanvas.g.fillRect(1, 1, 1, 1);
        // top rignt
        BaseCanvas.g.fillRect(w - 2, 1, 1, 1);
        // bot left
        BaseCanvas.g.fillRect(1, h - 2, 1, 1);
        // bot right
        BaseCanvas.g.fillRect(w - 2, h - 2, 1, 1);
        
        BaseCanvas.g.setColor(LIGHT);
        BaseCanvas.g.fillRect(2, 1, w - 4, 1);
        
//        BaseCanvas.g.setColor(0xffffff);
//        BaseCanvas.g.fillRect(2, 1, w - 4, 1);
//        BaseCanvas.g.fillRect(2, h - 2, w - 4, 1);
//        BaseCanvas.g.fillRect(1, 2, 1, h - 4);
//        BaseCanvas.g.fillRect(w - 2, 2, 1, h - 4);
//
//        BaseCanvas.g.drawRect(2, 2, w - 5, h - 5);
//
//        // outer border
//        BaseCanvas.g.setColor(0);
//        BaseCanvas.g.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
//        // inner border
//        BaseCanvas.g.drawRect(3, 3, w - 7, h - 7);
        BaseCanvas.g.translate(-x, -y);
    }

    public static void paintDialogStylePanel(int x, int y, int w, int h) {
        BaseCanvas.g.translate(x, y);
        BaseCanvas.g.setColor(LAF.CLR_BACKGROUND_DARKER);
        BaseCanvas.g.fillRect(3, 3, w - 6, h - 6);

        LAF.paintDialogBorder(0, 0, w, h);
        BaseCanvas.g.translate(-x, -y);
    }
    
    public static void paintDialogBackground(Dialog d) {
//        Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_BACKGROUND_DARKER, LAF.CLR_BACKGROUND_LIGHTER, 3, 3, d.w - 6, d.h - 2, false);
        BaseCanvas.g.setColor(DARK);
        BaseCanvas.g.fillRect(1, 1, d.w - 2, d.h - 2);
    }

    public static void paintDialogContent(Dialog d) {
    }

    public static void paintMessageDialogContent(MessageDialog d) {
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cac ham paint cho label.">
    public static void paintLabelBG(Label l) {
        if (l.isBlink && BaseCanvas.gameTicks % 10 > 3) {
            BaseCanvas.g.setColor(0xee0000);
            BaseCanvas.g.fillRect(0, 0, l.w, l.h);
        } else if (l.scrollType == 1) {
            BaseCanvas.g.setColor(0x0c3e88);
            BaseCanvas.g.fillRect(0, 0, l.w, l.h);
        }
    }

    public static void paintLabelBorder(Label l) {
    }

    public static void paintLabelContent(Label l) {
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac ham paint cho Button.">
    public static void paintButtonBackground(Button b) {
        //#if BigScreen || Android
//#         switch (b.type) {
//#             case 0:
//#                 break;
//#             case 7:
//#                 if (b.isPressed) {
//#                      Effects.drawLinearGradient(BaseCanvas.g,CLR_BUTTON_START ,CLR_BUTTON_END , 0, 0, b.w, b.h, false,LAF.LOT_ARC_SIZE);
//#                      BaseCanvas.g.setColor(0xFFFF00);
//#                      BaseCanvas.g.drawRoundRect(0, 0, b.w-1, b.h-1, LAF.LOT_ARC_SIZE, LAF.LOT_ARC_SIZE);
//#                 }else{
//#                     Effects.drawLinearGradient(BaseCanvas.g, CLR_BUTTON_END, CLR_BUTTON_START, 0, 0, b.w, b.h, false,LAF.LOT_ARC_SIZE);
//#                     
//#                 }
//#                 break;
//#             case 6://nut cho dialog.
//#                 if (b.isPressed) {
//# //                    Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_PRESSED_ITEM_LIGHTER,
//# //                            LAF.CLR_PRESSED_ITEM_DARKER, 1, 1, b.w - 3, b.h - 3, false);
//#                     BaseCanvas.g.drawRegion(Effects.createButtonBG(ResourceManager.imgButtonBg, b.w, b.h), 0, 0, b.w, b.h, Sprite.TRANS_MIRROR_ROT180, 0, 0, 0);
//#                 } else {
//# //                    Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_SELECTED_ITEM_DARKER,
//# //                            LAF.CLR_SELECTED_ITEM_LIGHTER, 1, 1, b.w - 3, b.h - 3, false);
//# //                    Effects.drawLinearGradient(BaseCanvas.g, 0xa31212,
//# //                            0x440101, 1, 1, b.w - 3, b.h - 3, false);
//#                     BaseCanvas.g.drawImage(Effects.createButtonBG(ResourceManager.imgButtonBg, b.w, b.h), 0, 0, 0);
//#                 }
//#                 break;
//#             case 5://nut menu item.
//#                 if (b.isPressed) {
//#                     Effects.drawLinearGradient(BaseCanvas.g, 0x4b779a, 0xfffffff0, 0, 0 + (b.h >> 1), b.w, b.h >> 1, false);
//# //                    Effects.drawLinearGradient(BaseCanvas.g, 0xf5ffffff, 0xba4b779a, 0, 0 + (h >> 1), w, h >> 1, false);
//# //                    Effects.drawLinearGradient(BaseCanvas.g, 0x4b779a, 0xffffffff, 0 + (w >> 1), 0, w >> 1, h >> 1, true);
//# //                    Effects.drawLinearGradient(BaseCanvas.g, 0xffffffff, 0x4b779a, 0, 0, w >> 1, h >> 1, true);
//#                 } else {
//#                     Effects.paintSeparator((b.w >> 1) - (b.normalfont.getWidth(b.text) >> 1), b.h - (b.border + b.padding), b.normalfont.getWidth(b.text), true);
//#                 }
//#                 break;
//#             case 4://balloon button.
//#                 if (b.isPressed) {
//# //                    Effects.drawLinearGradient(BaseCanvas.g, 0x4b779a, 0xffffffff, 0, 0 + (h >> 1), w, h >> 1, false);
//#                     Effects.drawLinearGradient(BaseCanvas.g, 0x4b779a, 0xffffffff, 0 + (b.w >> 1), 0, b.w >> 1, b.h, true);
//#                     Effects.drawLinearGradient(BaseCanvas.g, 0xffffffff, 0x4b779a, 0, 0, b.w >> 1, b.h, true);
//#                 }
//# 
//# 
//#                 break;
//#             case 1://check button.
//#             case 3://nut co hinh
//#                 break;
//#             default:
//#                 if (((b.focusBackgroundColor >> 24) & 0xff) != 0) {
//#                     BaseCanvas.g.setColor(b.focusBackgroundColor);
//#                     BaseCanvas.g.fillRoundRect(0, 0, b.w - 1, b.h - 1, 6, 6);
//#                 } else {
//#                     Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_SELECTED_ITEM_DARKER, LAF.CLR_SELECTED_ITEM_LIGHTER, 1, 1, b.w - 3, b.h - 3, false);
//#                 }
//#         }
//# //        if(type == 6) {
//# //
//# //        } else if (type == 4) {
//# //            return;
//# //        } else if (type == 5) {
//# //            Effects.paintSeparator((w >> 1) - (normalfont.getWidth(text) >> 1), h - (border + padding), normalfont.getWidth(text), true);
//# //            return;
//# //        } else if(type == 1 || type == 3) {
//# //        } else {
//# //            if (((focusBackgroundColor >> 24) & 0xff) != 0) {
//# //		BaseCanvas.g.setColor(focusBackgroundColor);
//# //		BaseCanvas.g.fillRoundRect(0, 0, w - 1, h - 1, 6, 6);
//# //	    } else {
//# //		Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_SELECTED_ITEM_DARKER, LAF.CLR_SELECTED_ITEM_LIGHTER, 1, 1, w - 3, h - 3, false);
//# //	    }
//# //        }
        //#else
        if (b.isPressed()) {
            //            Effects.drawLinearGradient(g, LAF.CLR_TITLE_DARKER, LAF.CLR_TITLE_LIGHTER, 0, 0, w - 1, h - 1, false);s
//            BaseCanvas.g.setColor(LAF.CLR_BORDER_FOCUSED);
//            BaseCanvas.g.drawRect(0, 0, w - 1, h - 1);
            if (((b.focusBackgroundColor >> 24) & 0xff) != 0) {
                BaseCanvas.g.setColor(b.focusBackgroundColor);
                BaseCanvas.g.fillRoundRect(0, 0, b.w - 1, b.h - 1, 6, 6);
            } else {
                //#if iwin_lite
//# 		BaseCanvas.g.setColor(LAF.CLR_PRESSED_ITEM_LIGHTER);
//# 		BaseCanvas.g.fillRect(1, 1, b.w - 2, b.h - 2);
                //#else
                Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_PRESSED_ITEM_LIGHTER, LAF.CLR_PRESSED_ITEM_DARKER, 1, 1, b.w - 2, b.h - 2, false);
                //#endif
            }
//            BaseCanvas.g.setColor(LAF.CLR_BORDER_FOCUSED);
//            BaseCanvas.g.drawRect(0, 0, w - 1, h - 1);
        } else if (b.isFocused && b.index == 0) {
//            if (((b.focusBackgroundColor >> 24) & 0xff) != 0) {
//                BaseCanvas.g.setColor(b.focusBackgroundColor);
//                BaseCanvas.g.fillRoundRect(0, 0, b.w - 1, b.h - 1, 6, 6);
//            } else {
//                Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_BACKGROUND_LIGHTER, LAF.CLR_BACKGROUND_DARKER, 1, 1, b.w - 2, b.h - 2, false);
//            }
            if (((b.focusBackgroundColor >> 24) & 0xff) != 0) {
                BaseCanvas.g.setColor(b.focusBackgroundColor);
                BaseCanvas.g.fillRoundRect(0, 0, b.w - 1, b.h - 1, 6, 6);
            } else {
                //#if iwin_lite
//# 		BaseCanvas.g.setColor(LAF.CLR_PRESSED_ITEM_LIGHTER);
//# 		BaseCanvas.g.fillRect(1, 1, b.w - 2, b.h - 2);
                //#else
                Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_PRESSED_ITEM_LIGHTER, LAF.CLR_PRESSED_ITEM_DARKER, 1, 1, b.w - 2, b.h - 2, false);
                //#endif
            }
        } else {
            if (((b.backgroundcolor >> 24) & 0xff) != 0) {
                BaseCanvas.g.setColor(b.backgroundcolor);
                BaseCanvas.g.fillRoundRect(0, 0, b.w - 1, b.h - 1, 6, 6);
            }
        }
        //#endif
    }

    public static void paintButtonBorder(Button b) {

//        if (b.border <= 0) {
//            return;
//        }
//        if (((b.borderColor >> 24) & 0xff) != 0) {
//            BaseCanvas.g.setColor(b.borderColor);
//        } else {
//            BaseCanvas.g.setColor(0x1d4088);
//        }
//        BaseCanvas.g.drawRoundRect(0, 0, b.w - 1, b.h - 1, 6, 6);
        if (b.isFocused) {
            BaseCanvas.g.setColor(CLR_BORDER_FOCUSED);
            BaseCanvas.g.drawRoundRect(1, 1, b.w - 3, b.h - 3, 6, 6);
        } else {
//            BaseCanvas.g.setColor(CLR_BORDER);
        }
//        BaseCanvas.g.drawRoundRect(1, 1, b.w - 3, b.h - 3, 6, 6);
    }

    public static void paintButtonContent(Button b) {
        if ((b.index == 1 || b.index == 2) && b.isSelected && b.image != null) {
            b.image.drawFrame(BaseCanvas.g, 2, 1,
                    ((b.preferredSize.height - b.image.frameHeight) >> 1), 0, BaseCanvas.TOPLEFT);
        }
        //#if BigScreen
//#         if (b.isPressed && b.type != 7) {
//#             int clipWidth = BaseCanvas.g.getClipWidth();
//#             int clipHeight = BaseCanvas.g.getClipHeight();
//#             int clipX = BaseCanvas.g.getClipX();
//#             int clipY = BaseCanvas.g.getClipY();
//#             BaseCanvas.g.setClip((BaseCanvas.instance.pointerPressedX - b.getAbsoluteX()) - (ResourceManager.imgFocus.getWidth() >> 1),
//#                     (BaseCanvas.instance.pointerPressedY - b.getAbsoluteY()) - (ResourceManager.imgFocus.getHeight() >> 1),
//#                     ResourceManager.imgFocus.getWidth(), ResourceManager.imgFocus.getHeight());
//#             BaseCanvas.g.drawImage(ResourceManager.imgFocus, BaseCanvas.instance.pointerPressedX - b.getAbsoluteX(), BaseCanvas.instance.pointerPressedY - b.getAbsoluteY(), BaseCanvas.CENTER);
//#             
//#             BaseCanvas.g.clipRect(clipX, clipY, clipWidth, clipHeight);
//#         }
        //#endif
    }

    public static void paintTabButtonBG(Button b) {
        if (LAF.mode == LAF.IWIN) {
            //<editor-fold defaultstate="collapsed" desc="Paint trong iwin.">
            if (b.isSelected || b.isFocused) {
                if (b.isFocused) {
                    BaseCanvas.g.setColor(b.focusBackgroundColor);
                } else {
                    BaseCanvas.g.setColor(CLR_MENU_BAR_LIGHTER);
                }
                BaseCanvas.g.fillRect(2, 2, b.w - 4, b.h - 2);
                // vien dam           
                BaseCanvas.g.setColor(0x323232);
                BaseCanvas.g.fillRect(2, 0, b.w - 4, 1);

                BaseCanvas.g.fillRect(0, 2, 1, b.h - 1);
                BaseCanvas.g.fillRect(b.w - 1, 2, 1, b.h - 1);

                BaseCanvas.g.fillRect(1, 1, 1, 1);
                BaseCanvas.g.fillRect(b.w - 2, 1, 1, 1);
            }
            //</editor-fold>
        } else {
            //<editor-fold defaultstate="collapsed" desc="Paint cho Mgo.">
            if (b.isSelected || b.isFocused) {
                if (b.isFocused) {
                    BaseCanvas.g.setColor(b.focusBackgroundColor);
                } else {
                    BaseCanvas.g.setColor(0xdcecff);//selected corlor.
                }
                BaseCanvas.g.fillRect(2, 2, b.w - 4, b.h - 2);
                BaseCanvas.g.setColor(0x323232);
                BaseCanvas.g.fillRect(2, 0, b.w - 4, 1);

                BaseCanvas.g.fillRect(0, 2, 1, b.h - 1);
                BaseCanvas.g.fillRect(b.w - 1, 2, 1, b.h - 1);

                BaseCanvas.g.fillRect(1, 1, 1, 1);
                BaseCanvas.g.fillRect(b.w - 2, 1, 1, 1);

                BaseCanvas.g.setColor(0xffffff);
                BaseCanvas.g.fillRect(2, 1, b.w - 4, 1);
                BaseCanvas.g.fillRect(1, 2, 1, b.h - 1);
                BaseCanvas.g.fillRect(b.w - 2, 2, 1, b.h - 1);

                BaseCanvas.g.fillRect(2, 2, 1, 1);
                BaseCanvas.g.fillRect(b.w - 3, 2, 1, 1);
            } else {

                BaseCanvas.g.setColor(b.backgroundcolor);

                BaseCanvas.g.fillRect(1, 3, b.w - 2, b.h - 2);

                BaseCanvas.g.setColor(0x636c67);
                BaseCanvas.g.fillRect(1, 2, b.w - 2, 1);
                BaseCanvas.g.fillRect(0, 3, 1, b.h - 2);
                BaseCanvas.g.fillRect(1, 3, 1, 1);
                BaseCanvas.g.fillRect(b.w - 2, 3, 1, 1);

                BaseCanvas.g.setColor(0x323232);
                BaseCanvas.g.fillRect(b.w - 1, 3, 1, b.h - 2);
            }
//</editor-fold>  
        }
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Cac ham paint cho EditField.">
    public static void paintEditFieldBorder(EditField ed) {
        //<editor-fold defaultstate="collapsed" desc="Paint trong iwin.">
        if (ed.isFocused) {            
            BaseCanvas.g.setColor(CLR_BORDER_FOCUSED);

        } else {
            BaseCanvas.g.setColor(CLR_BORDER);
        }     
//        //#if Android || BigScreen
//        if (ed.lableWidth == 0) {
//            BaseCanvas.g.drawRoundRect(0, 0, ed.w - 1, ed.h - 1, LOT_ARC_SIZE, LOT_ARC_SIZE);
//        } else {
//            BaseCanvas.g.drawRoundRect(0 + ed.lableWidth, 0, ed.w - 1 - ed.lableWidth, ed.h - 1, LOT_ARC_SIZE, LOT_ARC_SIZE);
//        }
//        //#else        
        if (ed.lableWidth == 0) {
            BaseCanvas.g.drawRoundRect(0, 0, ed.w - 1, ed.h - 1, LOT_ARC_SIZE, LOT_ARC_SIZE);
        } else {
            BaseCanvas.g.drawRoundRect(0 + ed.lableWidth, 0, ed.w - 1 - ed.lableWidth, ed.h - 1, LOT_ARC_SIZE, LOT_ARC_SIZE);
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Code ve trong mGo">
////        // vien den            
////        if (isFocused || border > 0) {
////            BaseCanvas.g.setColor(0x0d0d0d);
////            BaseCanvas.g.drawRoundRect(x1 + 1, y1 + 1, w1 - 2, h1 - 2, 4, 4);
////            BaseCanvas.g.fillRect(x1 + 2, y1 + 2, 1, 1);
////        }
////
//// vien xanh dam            
//        BaseCanvas.g.setColor(0x2e4b59);
//        BaseCanvas.g.drawRect(ed.x1, ed.y1, ed.w1, ed.h1);
//////      
////        if (isFocused || border > 0) {
////            BaseCanvas.g.setColor(0xffffff);
////            BaseCanvas.g.fillRect(x1 + 3, h1 - 1, w1 - 4, 1);
////            BaseCanvas.g.fillRect(w - 2, y1 + 3, 1, h1 - 4);
////        }
//
//        if (ed.isFocused) {
//// vien xanh focused
//            BaseCanvas.g.setColor(0x11b3ff);
//            BaseCanvas.g.drawRect(ed.x1 + 1, ed.y1 + 1, ed.w1 - 2, ed.h1 - 2);
////           BaseCanvas.g.fillRect(x1 + 3, y1 + 3, 1, 1);
////            BaseCanvas.g.fillRect(x1 + 3, h1 - 2, 1, 1);
////            BaseCanvas.g.fillRect(w - 3, y1 + 3, 1, 1);
////            BaseCanvas.g.fillRect(w - 3, h1 - 2, 1, 1);
//        }
//</editor-fold>
    }

    public static void paintEditFieldContent(EditField ed) {
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac hàm paint cho list item.">
    public static void paintListItemBG(ListItem li) {
        //<editor-fold defaultstate="collapsed" desc="Paint cho mGo.">
//        if (isPressed) {
//	    Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_PRESSED_ITEM_LIGHTER, LAF.CLR_PRESSED_ITEM_DARKER,
//		    0, 1, w, h, false);

//	} else if (isFocused) {
//	    Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_SELECTED_ITEM_DARKER, LAF.CLR_SELECTED_ITEM_LIGHTER,
//		    0, 1, w, h, false);
//	}
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Paint cho iwin.">
        if (li.isPressed()) {
            //#if iwin_lite
//# 	    BaseCanvas.g.setColor(LAF.CLR_PRESSED_ITEM_LIGHTER);
//# 	    BaseCanvas.g.fillRect(0, 0, li.w - 1, li.h - 1);
            //#else
            Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_PRESSED_ITEM_LIGHTER, LAF.CLR_PRESSED_ITEM_DARKER,
                    0, 0, li.w - 1, li.h - 1, false);
            //#endif
        } else if (li.isFocused) {
            //#if iwin_lite
//# 	    BaseCanvas.g.setColor(LAF.CLR_SELECTED_ITEM_DARKER);
//# 	    BaseCanvas.g.fillRect(0, 0, li.w - 1, li.h - 1);
            //#else
            Effects.drawLinearGradient(BaseCanvas.g, LAF.CLR_BACKGROUND_DARKER, LAF.CLR_BACKGROUND_LIGHTER,
                    0, 0, li.w - 1, li.h - 1, false);
            //#endif
        }
//</editor-fold>	
    }

    public static void paintListItemBorder(ListItem li) {
        //<editor-fold defaultstate="collapsed" desc="Paint trong iwin">
        BaseCanvas.g.setColor(0x555555);
        // ve phia duoi
//            BaseCanvas.g.drawLine(0, h - 1, w - 1, h - 1);
        // ve phia tren
        BaseCanvas.g.drawLine(0, 0, li.w - 1, 0);
        //                     Widget wid = getFocusedWidget(false);

        if (li.isFocused) {
            BaseCanvas.g.setColor(LAF.CLR_BORDER_FOCUSED);
//		BaseCanvas.g.drawRoundRect(0, 0, li.w - 1 - 2 * li.border, li.h - 1, LAF.LOT_ARC_SIZE, LAF.LOT_ARC_SIZE);
            BaseCanvas.g.drawRoundRect(0, 0, li.w - 1, li.h - 1, LAF.LOT_ARC_SIZE, LAF.LOT_ARC_SIZE);
        }
//</editor-fold>

    }

    public static void paintListItemContent(ListItem li) {
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cac ham paint cho Menu.">
    public static void paintMenuBorder(Menu m) {
//        if (mode == IWIN) {
//            BaseCanvas.g.setColor(CLR_BORDER_FOCUSED);
//            BaseCanvas.g.drawRoundRect(1, 1, m.w - 3, m.h - 3, 4, 4);
//            BaseCanvas.g.setColor(0x15181f);
//            BaseCanvas.g.drawRoundRect(2, 2, m.w - 5, m.h - 5, 3, 3);
//            BaseCanvas.g.setColor(CLR_BORDER_FOCUSED);
//        } else {
//            BaseCanvas.g.setColor(0x213e67);
//            BaseCanvas.g.drawRoundRect(0, 0, m.w - 1, m.h - 1, 6, 6);
//            BaseCanvas.g.setColor(0x5a7bac);
//            BaseCanvas.g.drawRoundRect(1, 1, m.w - 3, m.h - 3, 4, 4);
//            BaseCanvas.g.setColor(0x15181f);
//            BaseCanvas.g.drawRoundRect(2, 2, m.w - 5, m.h - 5, 3, 3);
//        }
        // whiter boder
        BaseCanvas.g.setColor(0xffffff);
        BaseCanvas.g.fillRect(2, 1, m.w - 4, 1);
        BaseCanvas.g.fillRect(2, m.h - 2, m.w - 4, 1);
        BaseCanvas.g.fillRect(1, 2, 1, m.h - 4);
        BaseCanvas.g.fillRect(m.w - 2, 2, 1, m.h - 4);

        BaseCanvas.g.drawRect(2, 2, m.w - 5, m.h - 5);

        // outer border
        BaseCanvas.g.setColor(0);
        BaseCanvas.g.drawRoundRect(0, 0, m.w - 1, m.h - 1, 10, 10);
        // inner border
        BaseCanvas.g.drawRect(3, 3, m.w - 7, m.h - 7);
    }

    public static void paintMenuBG(Menu m) {
        //#if !BigScreen && !Android
        BaseCanvas.g.setColor(LAF.CLR_MENU_BGR);
        BaseCanvas.g.fillRect(3, 3, m.w - 6, m.h - 6);
        //#endif
    }

    public static void paintMenuItemBackground() {
    }

    public static void paintMenuContent(Menu m) {
    }
//</editor-fold>

    /**
     * *
     * Paint thanh scroll bar.
     *
     * @param w
     */
    public static void paintScrollBar(Widget w) {
        if (w.scrollY != w.destScrollY || w.isDragActivated) {
            BaseCanvas.g.setClip(BaseCanvas.g.getClipX() + w.border, BaseCanvas.g.getClipY() + w.border,
                    BaseCanvas.g.getClipWidth() - (w.border << 1), BaseCanvas.g.getClipHeight() - (w.border << 1));
            BaseCanvas.g.setColor(0xbfbeac7);
            BaseCanvas.g.fillRoundRect(w.w - 4 - w.border, w.scrollBarY, 4, w.scrollBarH, 4, 4);
            BaseCanvas.g.setColor(0xde8223);
            BaseCanvas.g.fillRect(w.w - 3 - w.border, w.scrollBarY + 2, 2, w.scrollBarH - 4);
            BaseCanvas.g.setClip(BaseCanvas.g.getClipX() - w.border, BaseCanvas.g.getClipY() - w.border,
                    BaseCanvas.g.getClipWidth() + (w.border << 1), BaseCanvas.g.getClipHeight() + (w.border << 1));
        }
    }
}
