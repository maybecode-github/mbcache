# mbcache
simple cache library

**Usage:**

````
CacheMap<String, String> cacheMap = new MBCache<>();
cacheMap.expireAfter(5L, TimeUnit.SECONDS);

cacheMap.cache("test", "hello world");
cacheMap.cache("expire", "this cache expires and reloads", 3L, TimeUnit.SECONDS);
````
