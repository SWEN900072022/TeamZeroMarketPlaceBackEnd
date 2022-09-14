package Model;

import Entity.SellerGroup;
import Enums.UserRoles;
import Injector.FindGroupIdByNameInjector;
import Mapper.SellerGroupMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.List;

public class SellerGroupModel {
    private IUnitofWork<SellerGroup> sellerGroupRepo;

    public SellerGroupModel() {
        sellerGroupRepo = new Repository<SellerGroup>(new SellerGroupMapper());
    }

    public boolean createSellerGroup(SellerGroup sg, String jwt) {
        // Get the role from the jwt
        String role;

        try{
            if(!JWTUtil.validateToken(jwt)) {
                // if not valid, return false
                return false;
            }
            role = JWTUtil.getClaim("role", jwt);
        } catch (Exception e) {
            // Something went wrong
            return false;
        }

        // Check to see if the role is empty or null or not admin
        if(role == null || role == "" || role != UserRoles.ADMIN.toString()) {
            return false;
        }

        // Might be worthwhile to do a check to see if the name is taken
        List<Object> param = new ArrayList<>();
        param.add(sg.getGroupName());

        SellerGroup sgTemp = sellerGroupRepo.read(new FindGroupIdByNameInjector(), param);

        // At this point, we should be an admin, write to db
        if(sgTemp.isEmpty()) {
            sellerGroupRepo.registerNew(sg);
            sellerGroupRepo.commit();
            return true;
        }
        return false;
    }
}
