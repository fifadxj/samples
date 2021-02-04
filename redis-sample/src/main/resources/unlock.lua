--KEYS[1] ：需要加锁的key，这里需要是字符串类型。
--ARGV[1] ：锁的唯一标识

-- 如果key已经不存在，说明已经被解锁
if (redis.call('exists', KEYS[1]) == 0) then
    return "1";
end;
-- key和field不匹配，说明当前客户端线程没有持有锁，不能主动解锁。
if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then
    return "0";
end;
local counter = redis.call('hincrby', KEYS[1], ARGV[1], -1); --重入次数减1
if (counter <= 0) then -- 如果counter<=0说明最外层的重入锁已解锁
    redis.call('del', KEYS[1]);
end;
return "1";