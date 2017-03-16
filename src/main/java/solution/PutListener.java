package exercise3.solution;

import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureListener;

/**
 * Created by Andri on 15.03.2017.
 */
public class PutListener implements BaseFutureListener {

    private DirectCommunicationPeer dir;

    public PutListener(DirectCommunicationPeer dir) {
       this.dir = dir;
    }

    public void operationComplete(BaseFuture future) throws Exception {
        if(future.isSuccess()){
            dir.registered();
        }
    }

    public void exceptionCaught(Throwable t) throws Exception {
        System.err.println("ERROR: " + t.getMessage());
        t.printStackTrace();
    }
}
