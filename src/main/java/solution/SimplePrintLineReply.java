package exercise3.solution;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

/**
 * Created by Andri on 15.03.2017.
 */
public class SimplePrintLineReply implements ObjectDataReply {

    DirectCommunicationPeer peer;

    public SimplePrintLineReply(DirectCommunicationPeer peer){
        this.peer = peer;
    }

    public Object reply(PeerAddress sender, Object request) throws Exception {
        System.err.println("I'm "+ peer.peer().peerID() + "I just got the message [" + request
                + "] from " + sender.peerId());
        peer.getSink().receiveMessage(request.toString());
        return "OK";
    }
}
