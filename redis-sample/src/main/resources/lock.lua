--KEYS[1] ：需要加锁的key，这里需要是字符串类型。
--ARGV[1] ：锁的超时时间，防止死锁
--ARGV[2] ：锁的唯一标识

-- 检查是否key已经被占用
if (redis.call('exists', KEYS[1]) == 0) then
    redis.call('hset', KEYS[1], ARGV[2], 1); --初次加锁值=1
    redis.call('pexpire', KEYS[1], ARGV[1]);
    return "1";
end;
-- 检查是否重入锁
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then
    redis.call('hincrby', KEYS[1], ARGV[2], 1); --重入次数+1
    redis.call('pexpire', KEYS[1], ARGV[1]); --重新设置超时时间
    return "1";
end;
return "0";