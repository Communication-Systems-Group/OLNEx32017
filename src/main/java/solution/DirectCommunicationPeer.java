package solution;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andri on 15.03.2017.
 */
public class DirectCommunicationPeer {

    PeerDHT myself;
    private MessageSink sink;

    private CountDownLatch registerLatch = new CountDownLatch(1);

    public DirectCommunicationPeer(PeerDHT peer) {
        myself = peer;
        myself.peer().objectDataReply(new SimplePrintLineReply(this));
    }

    public DirectCommunicationPeer(Random id, int port) throws IOException {
       this(new PeerBuilderDHT(
                new PeerBuilder(new Number160(id))
                        .ports(port)
                        .start()
        ).start());
    }

    public void bootstrap(Peer peer, BaseFutureListener listener){
        FutureBootstrap fbst = myself.peer().bootstrap().peerAddress(peer.peerAddress()).start();
        fbst.addListener(listener);
    }

    public void register(String name) throws IOException, ClassNotFoundException {

        FuturePut fput = myself.put(Number160.createHash(name))
                .data(new Data(myself.peerAddress()))
                .start();
        fput.addListener(new PutListener(this));
    }

    public void sendMsg(String recipient, String msg) {
        FutureGet fget = myself.get(Number160.createHash(recipient)).start();
        fget.addListener(new PeerAddressListener(myself.peer(), msg));

    }

    public void registered(){
        registerLatch.countDown();
    }

    public void waitForRegistration() throws InterruptedException {
        registerLatch.await(2, TimeUnit.SECONDS);
    }

    public PeerDHT peer() {
        return myself;
    }

    public MessageSink getSink() {
        return sink;
    }

    public void setSink(MessageSink sink) {
        this.sink = sink;
    }
}
