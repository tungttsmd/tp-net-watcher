package com.tpservers.Services.Facade;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import tungtt.Console.Console;
import tungtt.Pool.Hooks.WorkerLifecycleHooker;
import tungtt.Pool.Hooks.JobLifecycleHooker;
import tungtt.Pool.Hooks.WorkerHeartbeatHooker;

import java.util.concurrent.atomic.AtomicBoolean;

public final class RedisService implements
    WorkerLifecycleHooker,
    JobLifecycleHooker,
    WorkerHeartbeatHooker {

    private RedisService() {
    }

    private static class Holder {

        static final AtomicBoolean started = new AtomicBoolean(false);

        static final String REDIS_HOST = ConfigService.REDIS_SERVER_HOST();
        static final int REDIS_PORT = ConfigService.REDIS_SERVER_PORT();

        static final int REDIS_POOL_THREAD_HEARTBEAT_EXPIRE = ConfigService.REDIS_POOL_THREAD_HEARTBEAT_EXPIRE();
        static final String REDIS_JOB_PREFIX_NAME = ConfigService.REDIS_JOB_PREFIX_NAME();
        static final String REDIS_POOL_NAMED_THREAD_PREFIX = ConfigService.REDIS_POOL_NAMED_THREAD_PREFIX();
        static final String REDIS_POOL_WORKER_PREFIX = ConfigService.REDIS_POOL_WORKER_PREFIX();

        static final int JEDIS_POOL_CONFIG_MAX_TOTAL = ConfigService.REDIS_POOL_CONFIG_MAX_TOTAL();
        static final int JEDIS_POOL_CONFIG_MAX_IDLE = ConfigService.REDIS_POOL_CONFIG_MAX_IDLE();
        static final int JEDIS_POOL_CONFIG_MIN_IDLE = ConfigService.REDIS_POOL_CONFIG_MIN_IDLE();

        static final RedisService INSTANCE = new RedisService();
        static final JedisPool POOL = buildRedisPool();

    }

    public static RedisService getInstance() {

        return Holder.INSTANCE;
    }

    /* ========= BOOT METHOD ========= */

    public static RedisService start() {

        if (!Holder.started.compareAndSet(false, true)) {
            ConsoleService.info("Redis service already started");
            return Holder.INSTANCE;
        }
        ConsoleService.info("Connecting to redis service...");
        return Holder.INSTANCE;
    }

    /* ========= GETTER ========= */
    
    public static JedisPool getPool() {
        return Holder.POOL;
    }

    public static Jedis getResource() {
        return Holder.POOL.getResource();
    }

    public static int getHeartbeatExpireTime() {
        return Holder.REDIS_POOL_THREAD_HEARTBEAT_EXPIRE;
    }

    public static String getJobPrefix() {
        return Holder.REDIS_JOB_PREFIX_NAME;
    }

    public static String getThreadPrefix() {
        return Holder.REDIS_POOL_WORKER_PREFIX;
    }

    public static String getThreadSetName() {
        return Holder.REDIS_POOL_NAMED_THREAD_PREFIX;
    }

    /* ========= BUILDER METHOD ========= */

    private static JedisPool buildRedisPool() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        
        poolConfig.setMaxTotal(Holder.JEDIS_POOL_CONFIG_MAX_TOTAL);
        poolConfig.setMaxIdle(Holder.JEDIS_POOL_CONFIG_MAX_IDLE);
        poolConfig.setMinIdle(Holder.JEDIS_POOL_CONFIG_MIN_IDLE);
        
        return new JedisPool(poolConfig, Holder.REDIS_HOST, Holder.REDIS_PORT);
    }

    /* ============ HOOKER NAMEDTHREAD LIFECYCLE ============ */

    @Override
    public void onWorkerStart(String name) {
        
        try (Jedis j = RedisService.getResource()) {
            j.sadd(threadsKey("started"), name);
        }
    }

    @Override
    public void onWorkerStop(String name) {
        
        try (Jedis j = RedisService.getResource()) {
            j.srem(threadsKey("started"), name);
            j.hset(
                threadsKey("stopped"),
                name,
                String.valueOf(Console.now())
            );
        }
    }

    @Override
    public void onWorkerError(String name) {
        
        try (Jedis j = RedisService.getResource()) {
            j.hset(
                threadsKey("error"),
                name,
                String.valueOf(Console.now())
            );
        }
    }

    @Override
    public void onJobSubmit(String job) {
        
        try (Jedis j = RedisService.getResource()) {
            j.hincrBy(jobsKey("submit"), job, 1);
        }
    }

    @Override
    public void onJobStart(String job, String worker) {
        
        try (Jedis j = RedisService.getResource()) {
            j.hset(jobsKey("running"), worker, job);
        }
    }

    @Override
    public void onJobSuccess(String job, String worker) {
        
        try (Jedis j = RedisService.getResource()) {
            j.hdel(jobsKey("running"), worker);
            j.hincrBy(jobsKey("success"), job, 1);
        }
    }

    @Override
    public void onJobError(String job, String worker, Throwable e) {
        
        try (Jedis j = RedisService.getResource()) {
            j.hdel(jobsKey("running"), worker);
            j.hincrBy(jobsKey("error"), job, 1);
        }
    }

    @Override
    public void onHeartbeat(String worker, long ts) {

        try (Jedis j = getResource()) {
            j.hset(
                threadsKey("heartbeat"),
                worker,
                String.valueOf(ts)
            );
            j.expire(
                threadsKey("heartbeat"),
                Holder.REDIS_POOL_THREAD_HEARTBEAT_EXPIRE
            );
        }
    }

    /* ============ HELPER ============ */
    
    private static String jobsKey(String suffix) {

        return Holder.REDIS_JOB_PREFIX_NAME + ":" + suffix;
    }
    
    private static String threadsKey(String suffix) {

        return Holder.REDIS_POOL_NAMED_THREAD_PREFIX + ":" + suffix;
    }

    

}