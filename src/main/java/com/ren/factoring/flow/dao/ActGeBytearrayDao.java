package com.ren.factoring.flow.dao;

import com.ren.factoring.flow.models.model.ActGeBytearray;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActGeBytearrayDao {

    ActGeBytearray selectByDeployId(@Param("deploymentId") String deploymentId);

}
