package com.ait.igenem;

import com.ait.igenem.model.Decision;

/**
 * Created by Emily on 12/2/16.
 */
public interface CreateDecisionDataInterface {

    void addDecisionToFirebase(Decision decision);
    void showDecisionActivity(Decision decision);
    void showFragmentByTag(String tag);
    String getUserName();
}
