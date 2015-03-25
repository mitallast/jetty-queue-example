package org.mitallast.queue.action.queue.dequeue;

import com.google.inject.Inject;
import org.mitallast.queue.action.AbstractAction;
import org.mitallast.queue.action.ActionListener;
import org.mitallast.queue.action.ActionRequestValidationException;
import org.mitallast.queue.common.settings.Settings;
import org.mitallast.queue.queue.QueueMessage;
import org.mitallast.queue.queue.transactional.TransactionalQueueService;
import org.mitallast.queue.queues.QueueMissingException;
import org.mitallast.queue.queues.transactional.TransactionalQueuesService;

import java.io.IOException;

public class DeQueueAction extends AbstractAction<DeQueueRequest, DeQueueResponse> {

    private final TransactionalQueuesService queuesService;

    @Inject
    public DeQueueAction(Settings settings, TransactionalQueuesService queuesService) {
        super(settings);
        this.queuesService = queuesService;
    }

    @Override
    public void execute(DeQueueRequest request, ActionListener<DeQueueResponse> listener) {
        ActionRequestValidationException validationException = request.validate();
        if (validationException != null) {
            listener.onFailure(validationException);
            return;
        }
        if (!queuesService.hasQueue(request.getQueue())) {
            listener.onFailure(new QueueMissingException(request.getQueue()));
        }
        TransactionalQueueService queueService = queuesService.queue(request.getQueue());
        try {
            QueueMessage message = queueService.lockAndPop();
            listener.onResponse(new DeQueueResponse(message));
        } catch (IOException e) {
            listener.onFailure(e);
        }
    }
}