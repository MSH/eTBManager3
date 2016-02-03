package org.msh.etbm.services.admin.usersws;

/**
 * Provide a list of options for User Workspace UI form, specifically, for the user view field
 *
 * Created by rmemoria on 3/2/16.
 */
//@Component
public class UserWsOptionsResolver { // implements OptionsResolver {

//    @Autowired
//    OptionsManagerService optionsManagerService;
//
//    @Autowired
//    UserRequestService userRequestService;
//
//    @PersistenceContext
//    EntityManager entityManager;
//
//    @PostConstruct
//    public void register() {
//        optionsManagerService.register("userViews", this);
//    }
//
//    @Override
//    public List<Item> getOptions(Map<String, Object> params) {
//        UserSession us = userRequestService.getUserSession();
//
//        List<Item> options = new ArrayList<>();
//        options.add(new Item<String>("workspace", us.getWorkspaceName()));
//
//        // get the unit id
//        String s = (String)params.get("unitId");
//        if (s == null) {
//            return options;
//        }
//        UUID unitId = UUID.fromString(s);
//
//        // get the unit
//        Unit unit = entityManager.find(Unit.class, unitId);
//        List<AdministrativeUnit> lst = unit.getAddress().getAdminUnit().getParentsTreeList(true);
//
//        for (AdministrativeUnit adm: lst) {
//            options.add(new Item("A" + adm.getId(), adm.getName()));
//        }
//        return options;
//    }
}
