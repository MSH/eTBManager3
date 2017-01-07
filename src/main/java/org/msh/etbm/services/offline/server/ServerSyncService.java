package org.msh.etbm.services.offline.server;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.services.offline.StatusResponse;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.data.ServerCredentialsData;
import org.msh.etbm.services.offline.fileimporter.FileImporter;
import org.msh.etbm.services.offline.server.data.SyncTrack;
import org.msh.etbm.services.offline.server.sync.ServerSyncPhase;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Mauricio on 07/01/2017.
 */
@Service
public class ServerSyncService {

    @Autowired
    SyncTracker tracker;

    @Autowired
    FileImporter importer;

    @Autowired
    ServerFileGenerator generator;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    Messages messages;

    public StatusResponse startSync(File clientSyncFile) {
        UUID unitId = userRequestService.getUserSession().getUnitId();
        SyncTrack track = tracker.startTracking(clientSyncFile, unitId);

        track.setPhase(ServerSyncPhase.IMPORTING_CLIENT_FILE);
        importer.importFile(clientSyncFile, true, null,
            (importedFile, fileVersion) -> afterImporting(track, fileVersion));

        return getStatus(track.getSyncToken());
    }

    private void afterImporting(SyncTrack track, Integer fileVersion) {
        track.setPhase(ServerSyncPhase.GENERATING_SERVER_FILE);
        File serverSyncFile = generator.generate(track.getUnitId(), Optional.of(fileVersion)).getFile();

        track.setServerSyncFile(serverSyncFile);
        track.setPhase(ServerSyncPhase.WAITING_SERVER_FILE_DOWNLOAD);
    }

    public File getResponseFile(String syncToken) {
        SyncTrack track = getTrack(syncToken);

        if (ServerSyncPhase.WAITING_SERVER_FILE_IMPORTING.equals(track.getPhase())) {
            track.setPhase(ServerSyncPhase.WAITING_SERVER_FILE_IMPORTING);
            return track.getServerSyncFile();
        }

        throw new SynchronizationException("Server sync file is not ready or was already downloaded.");
    }

    public void endSync(String syncToken) {
        SyncTrack track = getTrack(syncToken);

        if (ServerSyncPhase.WAITING_SERVER_FILE_IMPORTING.equals(track.getPhase())) {
            tracker.endTracking(track.getSyncToken());
        }

        throw new SynchronizationException("Track is not ready to end. Phase: " + messages.get(track.getPhase().getMessageKey()));
    }

    public StatusResponse getStatus(String syncToken) {
        SyncTrack track = getTrack(syncToken);

        ServerSyncPhase phase = track.getPhase();

        if (phase == null) {
            return null;
        }

        return new StatusResponse(phase.name(), messages.get(phase.getMessageKey()));
    }

    private SyncTrack getTrack(String syncToken) {
        if (syncToken == null || syncToken.isEmpty()) {
            throw new SynchronizationException("SyncToken must be informed.");
        }

        return tracker.getTrack(syncToken);
    }

}
