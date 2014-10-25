package org.mitallast.queue.client;

import com.google.inject.Inject;
import org.mitallast.queue.action.ActionListener;
import org.mitallast.queue.action.queue.delete.DeleteAction;
import org.mitallast.queue.action.queue.delete.DeleteRequest;
import org.mitallast.queue.action.queue.delete.DeleteResponse;
import org.mitallast.queue.action.queue.dequeue.DeQueueAction;
import org.mitallast.queue.action.queue.dequeue.DeQueueRequest;
import org.mitallast.queue.action.queue.dequeue.DeQueueResponse;
import org.mitallast.queue.action.queue.enqueue.EnQueueAction;
import org.mitallast.queue.action.queue.enqueue.EnQueueRequest;
import org.mitallast.queue.action.queue.enqueue.EnQueueResponse;
import org.mitallast.queue.action.queue.peek.PeekQueueAction;
import org.mitallast.queue.action.queue.peek.PeekQueueRequest;
import org.mitallast.queue.action.queue.peek.PeekQueueResponse;
import org.mitallast.queue.action.queue.stats.QueueStatsAction;
import org.mitallast.queue.action.queue.stats.QueueStatsRequest;
import org.mitallast.queue.action.queue.stats.QueueStatsResponse;

public class QueueClient {
    private final EnQueueAction enQueueAction;
    private final DeQueueAction deQueueAction;
    private final PeekQueueAction peekQueueAction;
    private final DeleteAction deleteAction;
    private final QueueStatsAction queueStatsAction;

    @Inject
    public QueueClient(EnQueueAction enQueueAction,
                       DeQueueAction deQueueAction,
                       PeekQueueAction peekQueueAction,
                       DeleteAction deleteAction,
                       QueueStatsAction queueStatsAction) {
        this.enQueueAction = enQueueAction;
        this.deQueueAction = deQueueAction;
        this.peekQueueAction = peekQueueAction;
        this.deleteAction = deleteAction;
        this.queueStatsAction = queueStatsAction;
    }

    public void enQueueRequest(EnQueueRequest request, ActionListener<EnQueueResponse> listener) {
        enQueueAction.execute(request, listener);
    }

    public void deQueueRequest(DeQueueRequest request, ActionListener<DeQueueResponse> listener) {
        deQueueAction.execute(request, listener);
    }

    public void queueStatsRequest(QueueStatsRequest request, ActionListener<QueueStatsResponse> listener) {
        queueStatsAction.execute(request, listener);
    }

    public void deleteRequest(DeleteRequest request, ActionListener<DeleteResponse> listener) {
        deleteAction.execute(request, listener);
    }

    public void peekQueueRequest(PeekQueueRequest request, ActionListener<PeekQueueResponse> listener) {
        peekQueueAction.execute(request, listener);
    }
}
