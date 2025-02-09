package edu.foobar.controllers;

import edu.foobar.dao.MembershipDao;
import edu.foobar.models.Membership;

public class MembershipController {
    private MembershipDao membershipDao;

    public MembershipController(MembershipDao membershipDao) {
        this.membershipDao = membershipDao;
    }

    public Membership getOrCreateMembership(String email){
       Membership membership = membershipDao.get(email);
       if(membership == null){
           membership = membershipDao.save(new Membership(email, 0));
       }
       return membership;
    }
}
