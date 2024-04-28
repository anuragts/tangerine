import redis
import socket
import threading
import time

class Counter(object):
    def __init__(self):
        self.val = 0
        self._lock = threading.Lock()

    def increment(self):
        with self._lock:
            self.val += 1

    def value(self):
        with self._lock:
            return self.val

command_counter_1 = Counter()
command_counter_2 = Counter()
command_counter_3 = Counter()

CLIENT_1: redis.Redis = redis.Redis(host="localhost", port=6379, db=0, decode_responses=True)
CLIENT_2: redis.Redis = redis.Redis(host="localhost", port=6378, db=0, decode_responses=True)

def send_command_to_kvstore(command: str, port: int):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost", port))
        s.sendall(command.encode())
        response = s.recv(1024)
        return response.decode()

def test_set_redis(client: redis.Redis, counter: Counter):
    key = "mykey" + str(counter.value())
    value = "Hello, World!"
    client.set(key, value)
    counter.increment()

def test_set_kvstore(counter: Counter, port: int):
    key = "mykey" + str(counter.value())
    value = "Hello, World!"
    send_command_to_kvstore(f"SET {key} {value}\n", port)
    counter.increment()

def test_speed():
    print("Testing speed...")
    
    start = time.time()
    test_set_redis(CLIENT_1, command_counter_1)
    test_set_kvstore(command_counter_2, 1111)
    test_set_redis(CLIENT_2, command_counter_3)
    while time.time() - start < 10:
        threading.Thread(target=test_set_redis, args=(CLIENT_1, command_counter_1)).start()
        threading.Thread(target=test_set_kvstore, args=(command_counter_2, 1111)).start()
        threading.Thread(target=test_set_redis, args=(CLIENT_2, command_counter_3)).start()

    print(f"Number of SET commands sent by Redis client (6378): {command_counter_3.value()}")
    print(f"Number of SET commands sent by Radish client (6379): {command_counter_1.value()}")
    print(f"Number of SET commands sent by Tangerine-kv client (1111): {command_counter_2.value()}")

test_speed()