package org.msh.etbm.services.offline.server.sync;

import org.msh.etbm.services.offline.SynchronizationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mauricio on 07/01/2017.
 */
@Service
public class SyncTracker {

    private Map<String, SyncTrack> syncTrackers;

    public SyncTrack startTracking(File clientSyncFile, UUID workspaceId, UUID userId) {
        if (syncTrackers == null) {
            syncTrackers = new HashMap<>();
        }

        String syncToken = UUID.randomUUID().toString();
        SyncTrack track = new SyncTrack(syncToken, clientSyncFile, workspaceId, userId);

        syncTrackers.put(syncToken, track);

        return track;
    }

    public SyncTrack setPhase(String syncToken, ServerSyncPhase phase) {
        SyncTrack track = getTrack(syncToken);

        if (track == null) {
            throw new SynchronizationException("Sync track don't exists.");
        }

        track.setPhase(phase);

        return track;
    }

    public void endTracking(String syncToken) {
        // checks if track exists
        getTrack(syncToken);

        syncTrackers.remove(syncToken);
    }

    public SyncTrack getTrack(String syncToken) {
        SyncTrack track = syncTrackers.get(syncToken);

        if (track == null) {
            throw new SynchronizationException("Sync track don't exists.");
        }

        return track;
    }

}
