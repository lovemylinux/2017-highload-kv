# 3 этап - тестирование с Нагрузкой


### PUT без перезаписи

#### 2 threads, 4 connections:
```sh
eorov@e-HP:~/PycharmProjects/2017-highload-kv/loadtest$ wrk --latency -c4 -d1m -s putWithoutOverwriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    42.71ms    2.03ms  99.44ms   85.78%
    Req/Sec    44.87      5.49    60.00     97.17%
  Latency Distribution
     50%   42.11ms
     75%   42.31ms
     90%   46.04ms
     99%   46.90ms
  5387 requests in 1.00m, 494.86KB read
Requests/sec:     89.72
Transfer/sec:      8.24KB
```

---



### GET без повторов:

#### 2 threads, 4 connections
```sh
eorov@e-HP:~/PycharmProjects/2017-highload-kv/loadtest$ wrk --latency -c4 -d1m -s getWithoutRepeats.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.16ms    2.12ms  48.09ms   92.46%
    Req/Sec    45.08      7.11    60.00     88.66%
  Latency Distribution
     50%   43.95ms
     75%   44.00ms
     90%   44.06ms
     99%   48.03ms
  2227 requests in 1.00m, 61.67MB read
  Socket errors: connect 0, read 740085, write 0, timeout 0
Requests/sec:     37.10
Transfer/sec:      1.03MB

```
---



### PUT c перезаписью

#### 2 threads, 4 connections
```sh
eorov@e-HP:~/PycharmProjects/2017-highload-kv/loadtest$ wrk --latency -c4 -d1m -s putWithOverwritting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    42.70ms    2.59ms 134.14ms   88.29%
    Req/Sec    44.92      5.91    60.00     95.00%
  Latency Distribution
     50%   42.08ms
     75%   42.30ms
     90%   45.97ms
     99%   47.27ms
  5392 requests in 1.00m, 495.32KB read
Requests/sec:     89.82
Transfer/sec:      8.25KB
```
---


### GET c перезаписью

#### 2 threads, 4 connections
```sh
eorov@e-HP:~/PycharmProjects/2017-highload-kv/loadtest$ wrk --latency -c4 -d1m -s getWithRepeats.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.15ms    1.50ms  48.12ms   94.01%
    Req/Sec    45.23      6.82    60.00     89.17%
  Latency Distribution
     50%   43.95ms
     75%   43.99ms
     90%   44.05ms
     99%   48.02ms
  5428 requests in 1.00m, 5.80MB read
Requests/sec:     90.45
Transfer/sec:     98.94KB

```
---




