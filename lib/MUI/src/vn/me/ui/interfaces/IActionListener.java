/**
 * Interface cho viec lang nghe su kien.
 */
package vn.me.ui.interfaces;

/**
 * @author Tam Dinh
 */
public interface IActionListener {
    
    /**
     * 
     * Thực hiện một Action
     * @param o : Gồm 2 phần
     * - Object : Source command ( Command nào cho cái này -> để gộp lại xử lý chung).
     * - Object : Source component ( Widget nào thực hiện hành động này -> data).
     * Nghĩa là : o = new Object{Src command, src widget }
     * Có thể định nghĩa cho các trường hợp đặc biệt khác.
     * 
     */
    public void actionPerformed(Object o);
    
    
    
}
