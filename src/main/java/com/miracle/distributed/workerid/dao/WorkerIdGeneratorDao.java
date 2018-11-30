package com.miracle.distributed.workerid.dao;

import java.util.Random;

import com.miracle.common.miracle_jedis.ShardedJedisWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class WorkerIdGeneratorDao {
	private final int MIN_WORKER_ID = 0;
	private final int MAX_WORKER_ID = 2^10;
	private final int WORKER_ID_KEY_EXPIRE_TIME = 5 * 60;
	
	@Autowired
	@Qualifier("kkcoreCache")
	private ShardedJedisWrapper openDataCache;
	
	
	private int getNextCandidateId(int currId)
	{
		return ((currId+1) % (MAX_WORKER_ID-MIN_WORKER_ID)) + MIN_WORKER_ID;
	}
	
	public int createWorkerId(String workerName)
	{
		int currId = (new Random()).nextInt(MAX_WORKER_ID-MIN_WORKER_ID) + MIN_WORKER_ID;
	
		boolean gotOne = false;
		while(!gotOne)
		{
			String workerIdKey = "WORKERID_" + workerName + "_" + String.valueOf(currId);
			if(openDataCache.STRINGS.setnx(workerIdKey, "1") == 1)
			{
				openDataCache.KEYS.expire(workerIdKey, WORKER_ID_KEY_EXPIRE_TIME);
				gotOne = true;
			}
			else
			{//try next candidate worker id
				currId = getNextCandidateId(currId);
			}
		}
		return currId;
	}
	
	public int renewWorkerId(String workerName, int workerId)
	{
		String workerIdKey = "WORKERID_" + workerName + "_" + String.valueOf(workerId);
		if(openDataCache.KEYS.expire(workerIdKey, WORKER_ID_KEY_EXPIRE_TIME) == 1)
		{//renew old one
			return workerId;
		}
		else
		{//create new one
			return createWorkerId(workerName);
		}
	}
}
