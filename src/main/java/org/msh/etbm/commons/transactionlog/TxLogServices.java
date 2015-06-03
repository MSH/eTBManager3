package org.msh.etbm.commons.transactionlog;

import org.apache.commons.beanutils.PropertyUtils;
import org.msh.etbm.entities.TransactionLog;
import org.msh.etbm.entities.UserLog;
import org.msh.etbm.entities.UserRole;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Transaction log services for recording of actions in the system
 * (new records, information changed, or simply actions).
 * <p/>
 * Simple to use: Start an action to be recorded with the #begin method.
 * It will return an object ActionTX. Set all relevant information to be
 * recorded in this object. When it's done, call method #end passing the
 * action as a parameter, and the transaction will be recorded with the
 * information stored in the action object
 *
 * Created by rmemoria on 8/4/15.
 */
//@Name("txLogServices")
//@BypassInterceptors
public class TxLogServices {

    /**
     * Start a new transaction to be record
     * @param eventName the event alias name
     * @return ActionTX that will store information about the transaction
     */
    public ActionTX begin(String eventName) {
        return begin(eventName, null, null);
    }

    /**
     * Start a new transaction to be recorded. Optionally an entity can be
     * passed, and the action tx object will be automatically initialized
     * with the entity to be recorded as updated
     * @param eventName the event alias name
     * @param entity the entity to have its state stored
     * @param action the action to track the entity. Possible values are NEW, EDIT or DELETE
     * @return
     */
    public ActionTX begin(String eventName, Object entity, RoleAction action) {
        ActionTX atx = new ActionTX();

        atx.setEventName(eventName);
        atx.setRoleAction(action);

        // entity was defined ?
        if (entity != null) {
            if (action == null) {
                throw new RuntimeException("Operation must be informed");
            }

            // "guess" the operation by its action
            Operation oper = null;
            switch (action) {
                case NEW: oper = Operation.NEW;
                    break;
                case EDIT: oper = Operation.EDIT;
                    break;
                case DELETE: oper = Operation.DELETE;
                    break;
                default:
                    oper = null;
                    //throw new RuntimeException("Invalid action " + action);
            }

            // values are captured only if operation is set
            if (oper != null) {
                atx.mapValues(entity, oper);
            }
            atx.setDescription(entity.toString());
            atx.setEntityClass(entity.getClass().getSimpleName());
            // get the entity ID by reflection
            try {
                Integer id = (Integer)PropertyUtils.getProperty(entity, "id");
                atx.setEntityId(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return atx;
    }

    /**
     * Finish the recording of the transaction and register it in the
     * transaction log table according to the information stored in the
     * actionTx
     * @param atx contains information about the transaction to be stored
     * @return instance of the {@link TransactionLog} registered
     */
/*
    public TransactionLog end(ActionTX atx) {
        if (atx.isSaved()) {
            throw new RuntimeException("Action was already used to record a transaction");
        }

        // prepare the action to be saved
        if (!atx.prepareForSaving()) {
            return null;
        }

        EntityManager em = App.getEntityManager();

        // get the user role assigned to the event name
        if (atx.getEventName() == null) {
            throw new RuntimeException("Event name not informed");
        }
        UserRole role = (UserRole) em.createQuery("from UserRole where name = :name")
                .setParameter("name", atx.getEventName())
                .getResultList().get(0);


        UserLog userLog = getUserLog();
        if (userLog == null)
            throw new RuntimeException("No user found for transaction log operation");

        // save the transaction log
        TransactionLog log = new TransactionLog();
        log.setAction(atx.getRoleAction());
        log.setEntityId(atx.getEntityId());
        log.setRole(role);
        log.setEntityDescription(atx.getDescription());
        log.setTransactionDate(new Date());
        log.setUser(userLog);
        log.setWorkspace(getWorkspaceLog());
        log.setAdminUnit(getAdminUnit());
        log.setUnit(getUnit());
        log.setTitleSuffix(atx.getTitleSuffix());
        log.setEntityClass(atx.getEntityClass());
        log.setComments(atx.generateDetails());

        em.persist(log);
        em.flush();

        Object entity = atx.getEntity();
        // update transaction information to the entity
        if (entity instanceof Transactional) {
            Transactional t = (Transactional)entity;
            t.setLastTransaction(log);
        }

        atx.notifySaved(log);

        return log;
    }
*/


    /**
     * Add the details of an action to an existing transaction. It's useful when you're executing a batch
     * processing where a transaction must be available since from the beginning, but more information
     * will be included to the transaction later
     * @param atx the content to add (just details)
     */
/*
    public void update(ActionTX atx) {
        if (!atx.isSaved()) {
            throw new RuntimeException("Transaction cannot be updated because it was not saved");
        }
        atx.prepareForSaving();

        EntityManager em = App.getEntityManager();

        TransactionLog tx = atx.getTransactionLog();
        tx = em.merge(tx);
        tx.setEntityClass( atx.getEntityClass() );
        tx.setEntityId( atx.getEntityId() );
        tx.setAction( atx.getRoleAction() );
        tx.setEntityDescription( atx.getDescription() );
        tx.setComments( atx.generateDetails() );

        em.persist(tx);
        em.flush();

        atx.notifySaved(tx);
    }
*/


    /**
     * Return the user to be used in log transactions
     * @return
     */
/*
    protected UserLog getUserLog() {
        UserLogin userLogin =  (UserLogin) Component.getInstance("userLogin");

        if (userLogin == null)
            return null;

        EntityManager em = App.getEntityManager();

        UserLog userLog = em.find(UserLog.class, userLogin.getUser().getId());

        // if the user log doesn't exist, create a new one
        if (userLog == null) {
            // save new user log information
            userLog = new UserLog();
            User user = userLogin.getUser();
            userLog.setId(user.getId());
            userLog.setName(user.getName());
            em.persist(userLog);
            return userLog;
        }

        return userLog;
    }
*/



    /**
     * Return the workspace to be used in log transactions
     * @return
     */
/*
    protected WorkspaceLog getWorkspaceLog() {
        Workspace ws = (Workspace)Component.getInstance("defaultWorkspace");
        if (ws.getId()!=null){
            WorkspaceLog wslog = App.getEntityManager().find(WorkspaceLog.class, ws.getId());

            if (wslog == null) {
                wslog = new WorkspaceLog();
                wslog.setId(ws.getId());
                wslog.setName(new LocalizedNameComp());
                wslog.getName().setName1(ws.getName().getName1());
                wslog.getName().setName2(ws.getName().getName2());
                App.getEntityManager().persist(wslog);
            }
            return wslog;
        }
        return null;
    }
*/


    /**
     * Return the TB unit of the current user
     * @return
     */
/*
    protected Tbunit getUnit() {
        UserWorkspace uw = null;
        try{
            uw = (UserWorkspace)Component.getInstance("userWorkspace");
            uw = App.getEntityManager().find(UserWorkspace.class, uw.getId());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Tbunit unit = App.getEntityManager().find(Tbunit.class, uw.getTbunit().getId());
        return unit;
    }
*/

    /**
     * @return
     */
/*
    protected AdministrativeUnit getAdminUnit() {
        UserWorkspace uw = null;
        try{
            uw = (UserWorkspace)Component.getInstance("userWorkspace");
            uw = App.getEntityManager().find(UserWorkspace.class, uw.getId());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        AdministrativeUnit adminUnit = App.getEntityManager().find(AdministrativeUnit.class, uw.getTbunit().getAdminUnit().getId());
        return adminUnit;
    }
*/


}
