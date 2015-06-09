/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsms;

/**
 *
 * @author bao
 */
public class AtomicItem extends Item {
    public int price;
    public final String code;
    public final String param;
    public String to;
    
    public AtomicItem(String text, int price, String sms, String param, String to) {
        super(text);
        this.price = price;
        this.code = sms;
        this.to = to;
        this.param = param;
    }
    
}
