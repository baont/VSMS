package vn.me.network;

public interface IGameMessageHandler {

    /** Xu lý khi message toi game hien tai*/
    public void onMessage(Message msg);

    /** 
     * Kiem tra xem co phai instance hien 
     * tai xu ly cho gameId nay hay khong?
     */
    public boolean isGameType(byte gameId);
}
