package com.miracle.distributed.workerid.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.distributed.workerid.dao.WorkerIdGeneratorDao;

@RestController
@Api(value="分布式WorkerId api", tags={"分布式WorkerId操作接口"})
public class WorkerIdController {

	@Autowired
	private WorkerIdGeneratorDao workIdDao;
	
	@ApiOperation("获取分布式WorkerId")
    @ApiImplicitParams({
    	@ApiImplicitParam(paramType="path",name="workerName",dataType="String",required=true,value="worker类别",defaultValue="worker"),
    })
    @ApiResponses({
    	@ApiResponse(code=400,message="请求参数没填好"),
    	@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value = "/api/workerId/{workerName}", method = RequestMethod.GET)
    public Integer fetchWorkerId(@PathVariable("workerName") String workerName) {
        return workIdDao.createWorkerId(workerName);
    }
	
	@ApiOperation("续约分布式WorkerId")
    @ApiImplicitParams({
    	@ApiImplicitParam(paramType="path",name="workerName",dataType="String",required=true,value="worker类别",defaultValue="worker"),
    })
    @ApiResponses({
    	@ApiResponse(code=400,message="请求参数没填好"),
    	@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/api/workerId/{workerName}", method = RequestMethod.POST)
	public Integer renewWorkerId(@PathVariable("workerName") String workerName,
			@RequestBody @ApiParam(name="分布式workerId",value="workerId",required=true) Integer workerId) {
		return workIdDao.renewWorkerId(workerName, workerId);
	}
}
