package exercise3.solution;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;

/**
 * Created by Andri on 15.03.2017.
 */
public class PeerAddressListener implements BaseFutureListener {

    private Peer peer;
    private String msg;

    public PeerAddressListener(Peer peer, String msg){
        this.peer = peer;
        this.msg = msg;
    }

    public void operationComplete(BaseFuture future) throws Exception {

        FutureGet fget = (FutureGet) future;
        if (future.isSuccess()) {
            System.out.println("Max Powers has the address: " + fget.data().object());
            PeerAddress address = (PeerAddress) fget.data().object();

            FutureDirect fdirect = peer.sendDirect(address).object(msg).start();
            fdirect.addListener(new BaseFutureListener<BaseFuture>() {
                public void operationComplete(BaseFuture future) throws Exception {
                    System.out.println("Message Sent");
                }

                public void exceptionCaught(Throwable t) throws Exception {

                }
            });
        }else {
            System.err.println("Peer address failed to be retrieved");
        }
    }

    public void exceptionCaught(Throwable t) throws Exception {
        System.err.println("ERROR: " + t.getMessage());
        t.printStackTrace();
    }
}
