package org.mitallast.queue.raft.protocol;

import org.mitallast.queue.common.stream.StreamInput;
import org.mitallast.queue.common.stream.StreamOutput;
import org.mitallast.queue.common.stream.Streamable;
import org.mitallast.queue.raft.RaftMessage;
import org.mitallast.queue.transport.DiscoveryNode;

import java.io.IOException;

public class ClientMessage implements RaftMessage {
    private final DiscoveryNode client;
    private final Streamable cmd;

    public ClientMessage(StreamInput stream) throws IOException {
        client = stream.readStreamable(DiscoveryNode::new);
        cmd = stream.readStreamable();
    }

    public ClientMessage(DiscoveryNode client, Streamable cmd) {
        this.client = client;
        this.cmd = cmd;
    }

    public DiscoveryNode getClient() {
        return client;
    }

    public Streamable getCmd() {
        return cmd;
    }

    @Override
    public void writeTo(StreamOutput stream) throws IOException {
        stream.writeStreamable(client);
        stream.writeClass(cmd.getClass());
        stream.writeStreamable(cmd);
    }
}