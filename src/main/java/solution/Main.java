package solution;

import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureListener;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andri on 15.03.2017.
 */
public class Main implements BaseFutureListener, MessageSink {
    static int startPort = 11000;
    static int putPeer = 5;
    static int getPeer = 6;



    protected CountDownLatch counter;
    protected DirectCommunicationPeer[] peers;
    private CountDownLatch messageLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        new Main().start(10);
    }

    public void start(int numberPeers) throws IOException, ClassNotFoundException, InterruptedException {
        Random rnd = new Random();
        peers = new DirectCommunicationPeer[numberPeers];
        counter = new CountDownLatch(numberPeers - 1);
        peers[0] = new DirectCommunicationPeer(rnd, startPort);
        try {
            for (int i = 1; i < peers.length; i++) {
                    peers[i] = new DirectCommunicationPeer(rnd,startPort + i);
                    peers[i].bootstrap(peers[0].peer().peer(), this);
            }

            messageLatch.await(2, TimeUnit.SECONDS);

        } finally {
            Thread.sleep(30000);
            for (DirectCommunicationPeer p : peers) {
                p.peer().shutdown();
            }
        }
    }

    public void operationComplete(BaseFuture future) throws Exception {
        counter.countDown();
        System.out.println("Peer bootstrapped");
        if (counter.getCount() == 0) {
            System.out.println("Bootstrap succeeded");
            peers[putPeer].register("Max Powers");
            peers[putPeer].waitForRegistration();
            peers[putPeer].setSink(this);
            peers[getPeer].sendMsg("Max Powers", "Mr. X says hi");
        }
    }

    public void exceptionCaught(Throwable t) throws Exception {
        System.err.println("Error occurred: " + t.getMessage());
    }


    public void receiveMessage(String msg) {
        messageLatch.countDown();
    }
}
