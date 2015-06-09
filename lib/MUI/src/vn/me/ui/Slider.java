package vn.me.ui;

import vn.me.ui.interfaces.IActionListener;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import vn.me.core.BaseCanvas;
import vn.me.ui.common.Effects;
import vn.me.ui.common.ResourceManager;

/**
 *
 * @author TamDinh
 */
public class Slider extends Widget {

    private final static int HEAD_W = 3;
    //private final static int SLIDER_H = 20;
    /**
     * Gia tri lon nhat cua slider
     */
    public byte maxValue = 100;
    /**
     * Gia tri hien tai cua slider.
     */
    public int value = 0;
    public Image imgLeft;// = Image.createImage(ImageManager.slider, 0, 0, HEAD_W, h, Sprite.TRANS_NONE);
    public Image imgRight;// = Image.createImage(ImageManager.slider, HEAD_W + 2, 0, HEAD_W, h, Sprite.TRANS_NONE);
    public Image imgFillLeft;// = Image.createImage(ImageManager.slider, HEAD_W, 0, 1, h, Sprite.TRANS_NONE);
    public Image imgFillRight;//= Image.createImage(ImageManager.slider, HEAD_W + 1, 0, 1, h, Sprite.TRANS_NONE);
    public IActionListener valueChangedHandler;
    public boolean isDrawThumb = true;
    private int thumbW;
    private Image cacheScaleLeft, cacheScaleRight;
    /**
     * info show cung voi thanh slider, nhu cho biet so % hien thoi chang han.
     */
    private String info = null;
    private Font infoFont = null;
//	private int paddingRight = 0;//mGo

    public Slider(int x, int y, int w, String info, Font font) {
        if (info != null && font != null) {
            this.info = info;
            this.infoFont = font;
            if (ResourceManager.sliderThumb == null) {
                setMetrics(x, y, w, ResourceManager.slider.getHeight() + font.getHeight() + 1);
            } else {
                setMetrics(x, y, w, Math.max(ResourceManager.slider.getHeight(),
                        ResourceManager.sliderThumb.getHeight()) + font.getHeight() + 1);
            }
        } else if (ResourceManager.sliderThumb == null) {
            setMetrics(x, y, w, ResourceManager.slider.getHeight());
        } else {
            setMetrics(x, y, w, Math.max(ResourceManager.slider.getHeight(), ResourceManager.sliderThumb.getHeight()));
        }
//	imgLeft = Image.createImage(ResourceManager.slider, 0, 0, HEAD_W, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
//	imgRight = Image.createImage(ResourceManager.slider, HEAD_W + 2, 0, HEAD_W, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
//	imgFillLeft = Image.createImage(ResourceManager.slider, HEAD_W, 0, 1, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
//	imgFillRight = Image.createImage(ResourceManager.slider, HEAD_W + 1, 0, 1, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
        imgLeft = Image.createImage(ResourceManager.slider, 0, 0, HEAD_W, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
        imgRight = Image.createImage(ResourceManager.slider, HEAD_W, 0, HEAD_W, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
        imgFillLeft = Image.createImage(ResourceManager.slider, HEAD_W - 1, 0, 1, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
        imgFillRight = Image.createImage(ResourceManager.slider, HEAD_W, 0, 1, ResourceManager.slider.getHeight(), Sprite.TRANS_NONE);
        isFocusable = true;
        thumbW = ResourceManager.slider.getWidth();
//	paddingRight = 3;//Styles.LOT_PADDING;
    }

    /**
     *
     * @param x
     * @param y
     * @param w
     */
    public Slider(int x, int y, int w) {
        this(x, y, w, null, null);
    }

    /**
     * @inheritDoc @param type
     * @param keyCode
     * @return
     */
    public boolean checkKeys(int type, int keyCode) {
        if (type == 0 && keyCode == BaseCanvas.KEY_LEFT) {
            return decrease(2);
        } else if (type == 0 && keyCode == BaseCanvas.KEY_RIGHT) {
            return increase(2);
        }
        return false;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @inheritDoc
     */
    public void paint() {
        int sliderLenght = w - thumbW - (HEAD_W << 1);
        int scaleLength = (value * sliderLenght / maxValue);

        int sliderH = ResourceManager.slider.getHeight();

        int sliderY = infoFont != null ? infoFont.getHeight() + 1 : ((h - ResourceManager.slider.getHeight()) >> 1);
        if (scaleLength > HEAD_W) {
            BaseCanvas.g.drawImage(imgLeft, thumbW >> 1, sliderY, BaseCanvas.TOPLEFT);
            BaseCanvas.g.drawImage(getCacheScaleLeft(scaleLength, sliderH), (thumbW >> 1) + HEAD_W, sliderY, BaseCanvas.TOPLEFT);
        } else {
            BaseCanvas.g.drawRegion(imgRight, 0, 0, HEAD_W, sliderH, Sprite.TRANS_ROT180, thumbW >> 1, sliderY, BaseCanvas.TOPLEFT);
        }

        if (sliderLenght - scaleLength > HEAD_W) {
            BaseCanvas.g.drawImage(getCacheScaleRight(sliderLenght - scaleLength, sliderH), (thumbW >> 1) + HEAD_W + scaleLength, sliderY, BaseCanvas.TOPLEFT);
            BaseCanvas.g.drawImage(imgRight, w - (thumbW >> 1) - HEAD_W, sliderY, BaseCanvas.TOPLEFT);
        } else {
            BaseCanvas.g.drawRegion(imgLeft, 0, 0, HEAD_W, sliderH, Sprite.TRANS_ROT180, w - (thumbW >> 1) - HEAD_W, sliderY, BaseCanvas.TOPLEFT);
        }
        if (isDrawThumb && ResourceManager.sliderThumb != null) {
            BaseCanvas.g.drawImage(ResourceManager.sliderThumb, (thumbW >> 1) + HEAD_W + scaleLength, /*
                     * sliderY + ((h - sliderY) >> 1)
                     */ 0, Graphics.HCENTER | Graphics.TOP);
        }
        if (infoFont != null) {
//            System.out.println("slider print: " + info + " voi font: " + infoFont.getHeight());
            infoFont.drawString(BaseCanvas.g, info, (thumbW >> 1) + HEAD_W + scaleLength, 0, Font.CENTER);
        }
//	int sliderLenght = w - thumbW;
//	int scaleLength = (value * sliderLenght / maxValue);
//	
//        int sliderH = ResourceManager.slider.getHeight();
//        
//	if (scaleLength > HEAD_W) {
//            BaseCanvas.g.drawImage(imgLeft, thumbW >> 1, 0, BaseCanvas.TOPLEFT);
//            if (sliderLenght - scaleLength > HEAD_W) {
//                BaseCanvas.g.drawImage(getCacheScaleLeft(scaleLength - HEAD_W, sliderH), (thumbW >> 1) + HEAD_W, 0, BaseCanvas.TOPLEFT);
//            } else { //ve cham mod ben phai.
//                BaseCanvas.g.drawImage(getCacheScaleLeft(sliderLenght - (HEAD_W << 1), sliderH), (thumbW >> 1) + HEAD_W, 0, BaseCanvas.TOPLEFT);
//            }
//	} else {
//	    BaseCanvas.g.drawRegion(imgRight, 0, 0, HEAD_W, sliderH, Sprite.TRANS_ROT180, thumbW >> 1, 0, BaseCanvas.TOPLEFT);
//	}
//        
//	if (sliderLenght - scaleLength > HEAD_W) {
//            if (scaleLength > HEAD_W) {
//                BaseCanvas.g.drawImage(getCacheScaleRight(sliderLenght - scaleLength - HEAD_W, sliderH), (thumbW >> 1) + scaleLength, 0, BaseCanvas.TOPLEFT);
//            } else {//ve cham mod ben trai
//                BaseCanvas.g.drawImage(getCacheScaleRight(sliderLenght - (HEAD_W << 1), sliderH), (thumbW >> 1) + HEAD_W, 0, BaseCanvas.TOPLEFT);
//            }
//	    BaseCanvas.g.drawImage(imgRight, w - (thumbW >> 1) - HEAD_W, 0, BaseCanvas.TOPLEFT);
//	} else {
//	    BaseCanvas.g.drawRegion(imgLeft, 0, 0, HEAD_W, sliderH, Sprite.TRANS_ROT180, w - (thumbW >> 1) - HEAD_W, 0, BaseCanvas.TOPLEFT);
//	}
//	if (isDrawThumb && ResourceManager.sliderThumb != null) {
//	    BaseCanvas.g.drawImage(ResourceManager.sliderThumb, (thumbW >> 1) + scaleLength, (sliderH >> 1), Graphics.HCENTER | Graphics.VCENTER);
//	}

//        if (isFocused && (BaseCanvas.gameTicks % 10 > 2)) {
//            ResourceManager.boldFont.drawString( BaseCanvas.g, "-", x - 15, y + (SLIDER_H >> 1) - (Font.boldFont.getHeight() >> 1), 0);
//            ResourceManager.boldFont.drawString( BaseCanvas.g, "+", x + w + 3, y + (SLIDER_H >> 1) - (Font.boldFont.getHeight() >> 1), 0);
//        }

    }

    /**
     * Tăng giá trị của slider.
     *
     * @param step Độ tăng của slider.
     * @return true-Đã tăng, ngược lại không tăng được.
     */
    public boolean increase(int step) {
        if (value == maxValue) {
            return false;
        }
        value += step;
        if (value >= maxValue) {
            value = maxValue;
//            paddingRight = 3;//Styles.LOT_PADDING;
        }
        if (valueChangedHandler != null) {
            valueChangedHandler.actionPerformed(new Object[]{null, this});
        }
        cacheScaleLeft = null;
        cacheScaleRight = null;
        return true;
    }

    private Image getCacheScaleLeft(int scaleW, int sliderH) {
        if (cacheScaleLeft == null) {
//	    int sliderLenght = w - thumbW;
//	    int scaleLength = (value * sliderLenght / maxValue);
//	    int sliderH = ResourceManager.slider.getHeight();
//            if (sliderLenght - scaleLength >= HEAD_W) {
//                cacheScaleLeft = Effects.scale(imgFillLeft, scaleLength - HEAD_W, sliderH);
//            } else {
//                cacheScaleLeft = Effects.scale(imgFillLeft, scaleLength - (HEAD_W << 1), sliderH);
//            }
            cacheScaleLeft = Effects.scale(imgFillLeft, scaleW, sliderH);
        }
        return cacheScaleLeft;
    }

    private Image getCacheScaleRight(int scaleW, int sliderH) {
        if (cacheScaleRight == null) {
//	    int sliderLenght = w - thumbW;
//	    int scaleLength = (value * sliderLenght / maxValue);
//	    int sliderH = ResourceManager.slider.getHeight();
//            if (scaleLength > HEAD_W) {
//                cacheScaleRight = Effects.scale(imgFillRight, sliderLenght - scaleLength - HEAD_W, sliderH);
//            } else {
//                cacheScaleRight = Effects.scale(imgFillRight, sliderLenght - scaleLength - (HEAD_W << 1), sliderH);
//            }
            cacheScaleRight = Effects.scale(imgFillRight, scaleW, sliderH);
        }
        return cacheScaleRight;
    }

    /**
     * Giảm giá trị của slider.
     *
     * @param step Độ giảm của slider.
     * @return true-đã giảm. false-chưa giảm.
     */
    public boolean decrease(int step) {
//        paddingRight = 0;
        if (value == 0) {
            return false;
        }
        value -= step;
        if (value < 0) {
            value = 0;
        }
        if (valueChangedHandler != null) {
            valueChangedHandler.actionPerformed(new Object[]{null, this});
        }
        cacheScaleLeft = null;
        cacheScaleRight = null;
        return true;
    }

    /**
     * @inheritDoc @param x
     * @param y
     * @return
     */
    public boolean pointerPressed(int x, int y) {
        super.pointerPressed(x, y);
        value = (x - (getAbsoluteX() + (thumbW >> 1))) * maxValue / (w - thumbW);
        if (value < 0) {
            value = 0;
        }
        if (value > maxValue) {
            value = maxValue;
        }
        valueChangedHandler.actionPerformed(new Object[]{null, this});
        cacheScaleLeft = null;
        cacheScaleRight = null;
        return true;
    }

    /**
     * @inheritDoc @param x
     * @param y
     * @return
     */
    public boolean pointerDragged(int x, int y) {
        ((Screen) BaseCanvas.getCurrentScreen()).draggedWidget = this;
        isDragActivated = true;
        return pointerPressed(x, y);
    }
}
