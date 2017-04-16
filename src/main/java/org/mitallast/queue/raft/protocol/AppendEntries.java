package org.mitallast.queue.raft.protocol;

import javaslang.collection.Vector;
import org.mitallast.queue.common.stream.StreamInput;
import org.mitallast.queue.common.stream.StreamOutput;
import org.mitallast.queue.common.stream.Streamable;
import org.mitallast.queue.transport.DiscoveryNode;

public class AppendEntries implements Streamable {
    private final DiscoveryNode member;
    private final long term;
    private final long prevLogTerm;
    private final long prevLogIndex;
    private final long leaderCommit;
    private final Vector<LogEntry> entries;

    public AppendEntries(StreamInput stream) {
        member = stream.readStreamable(DiscoveryNode::new);
        term = stream.readLong();
        prevLogTerm = stream.readLong();
        prevLogIndex = stream.readLong();
        leaderCommit = stream.readLong();
        entries = stream.readVector(LogEntry::new);
    }

    public AppendEntries(DiscoveryNode member, long term, long prevLogTerm, long prevLogIndex, Vector<LogEntry> entries, long leaderCommit) {
        this.member = member;
        this.term = term;
        this.prevLogTerm = prevLogTerm;
        this.prevLogIndex = prevLogIndex;
        this.leaderCommit = leaderCommit;
        this.entries = entries;
    }

    @Override
    public void writeTo(StreamOutput stream) {
        stream.writeStreamable(member);
        stream.writeLong(term);
        stream.writeLong(prevLogTerm);
        stream.writeLong(prevLogIndex);
        stream.writeLong(leaderCommit);
        stream.writeVector(entries);
    }

    public DiscoveryNode getMember() {
        return member;
    }

    public long getTerm() {
        return term;
    }

    public long getPrevLogTerm() {
        return prevLogTerm;
    }

    public long getPrevLogIndex() {
        return prevLogIndex;
    }

    public long getLeaderCommit() {
        return leaderCommit;
    }

    public Vector<LogEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppendEntries that = (AppendEntries) o;

        if (prevLogIndex != that.prevLogIndex) return false;
        if (leaderCommit != that.leaderCommit) return false;
        if (!member.equals(that.member)) return false;
        if (term != that.term) return false;
        if (prevLogTerm != that.prevLogTerm) return false;
        return entries.equals(that.entries);

    }

    @Override
    public int hashCode() {
        int result = member.hashCode();
        result = 31 * result + (int) (term ^ (term >>> 32));
        result = 31 * result + (int) (prevLogTerm ^ (prevLogTerm >>> 32));
        result = 31 * result + (int) (prevLogIndex ^ (prevLogIndex >>> 32));
        result = 31 * result + (int) (leaderCommit ^ (leaderCommit >>> 32));
        result = 31 * result + entries.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AppendEntries{" +
            "member=" + member +
            ", term=" + term +
            ", prevLogTerm=" + prevLogTerm +
            ", prevLogIndex=" + prevLogIndex +
            ", leaderCommit=" + leaderCommit +
            ", entries=" + entries +
            '}';
    }
}
