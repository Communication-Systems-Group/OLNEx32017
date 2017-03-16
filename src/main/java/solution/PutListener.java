package solution;

import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureListener;

/**
 * Created by Andri on 15.03.2017.
 */
public class PutListener implements BaseFutureListener {

    private DirectCommunicationPeer peer;

    public PutListener(DirectCommunicationPeer peer) {
       this.peer = peer;
    }

    public void operationComplete(BaseFuture future) throws Exception {
        if(future.isSuccess()){
            peer.registered();
        }
    }

    public void exceptionCaught(Throwable t) throws Exception {
        System.err.println("ERROR: " + t.getMessage());
        t.printStackTrace();
    }
}
