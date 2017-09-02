package org.mitallast.queue.raft.protocol;

import org.mitallast.queue.common.codec.Codec;
import org.mitallast.queue.common.codec.Message;
import org.mitallast.queue.transport.DiscoveryNode;

public class AppendSuccessful implements Message {
    public static final Codec<AppendSuccessful> codec = Codec.Companion.of(
        AppendSuccessful::new,
        AppendSuccessful::getMember,
        AppendSuccessful::getTerm,
        AppendSuccessful::getLastIndex,
        DiscoveryNode.codec,
        Codec.Companion.longCodec(),
        Codec.Companion.longCodec()
    );

    private final DiscoveryNode member;
    private final long term;
    private final long lastIndex;

    public AppendSuccessful(DiscoveryNode member, long term, long lastIndex) {
        this.member = member;
        this.term = term;
        this.lastIndex = lastIndex;
    }

    public DiscoveryNode getMember() {
        return member;
    }

    public long getTerm() {
        return term;
    }

    public long getLastIndex() {
        return lastIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppendSuccessful that = (AppendSuccessful) o;

        if (term != that.term) return false;
        if (lastIndex != that.lastIndex) return false;
        return member.equals(that.member);
    }

    @Override
    public int hashCode() {
        int result = member.hashCode();
        result = 31 * result + (int) (term ^ (term >>> 32));
        result = 31 * result + (int) (lastIndex ^ (lastIndex >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "AppendSuccessful{" +
            "member=" + member +
            ", term=" + term +
            ", lastIndex=" + lastIndex +
            '}';
    }
}
