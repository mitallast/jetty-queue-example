package org.mitallast.queue.action.queues.stats;

import com.google.inject.Inject;
import org.mitallast.queue.action.AbstractAction;
import org.mitallast.queue.action.ActionListener;
import org.mitallast.queue.action.ActionRequestValidationException;
import org.mitallast.queue.common.settings.Settings;
import org.mitallast.queue.queues.stats.QueuesStats;
import org.mitallast.queue.queues.transactional.TransactionalQueuesService;

import java.io.IOException;

public class QueuesStatsAction extends AbstractAction<QueuesStatsRequest, QueuesStatsResponse> {

    private final TransactionalQueuesService queuesService;

    @Inject
    public QueuesStatsAction(Settings settings, TransactionalQueuesService queuesService) {
        super(settings);
        this.queuesService = queuesService;
    }

    @Override
    public void execute(QueuesStatsRequest request, ActionListener<QueuesStatsResponse> listener) {
        ActionRequestValidationException validationException = request.validate();
        if (validationException != null) {
            listener.onFailure(validationException);
            return;
        }
        try {
            QueuesStats stats = queuesService.stats();
            listener.onResponse(new QueuesStatsResponse(stats));
        } catch (IOException e) {
            listener.onFailure(e);
        }
    }
}
