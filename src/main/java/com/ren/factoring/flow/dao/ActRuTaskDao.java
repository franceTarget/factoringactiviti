package com.ren.factoring.flow.dao;

import com.ren.factoring.flow.models.model.ActRuTask;
import org.springframework.stereotype.Repository;

@Repository
public interface ActRuTaskDao {

    public ActRuTask getByProcInstId(String procInstId);
}
