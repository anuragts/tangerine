# 🍊 tangerine.
## in-memory vector database

## Running

Making sh executable
```bash
chmod +x tangerinekv-compile.sh 
chmod +x tangerinekv-server.sh
chmod +x tangerinekv-cli.sh
```

> [!NOTE]
> The .java files are already compiled, but to prevent any issues with the classpath, you should compile them first.

```bash
$ ./tangerinekv-compile.sh
```

```bash
$ ./tangerinekv-server.sh
```

```bash
$ ./tangerinekv-cli.sh
```

## Commands./src/kv/KVServer.java:8: error: cannot find symbol
    private InMemoryStorage storage;
            ^
  symbol:   class InMemoryStorage
  location: class KVServer
./src/kv/KVServer.java:11: error: cannot find symbol
    public KVServer(InMemoryStorage storage, int port) throws IOException {
                    ^
  symbol:   class InMemoryStorage
  location: class KVServer
./src/kv/KVServer.java:60: error: cannot find symbol
        InMemoryStorage storage = new InMemoryStorage();
        ^
  symbol:   class InMemoryStorage
  location: class KVServer
./src/kv/KVServer.java:60: error: cannot find symbol
        InMemoryStorage storage = new InMemoryStorage();
                                      ^
  symbol:   class InMemoryStorage
  location: class KVServer
4 errors

### set

```bash
$ tangerine-cli set key value
```

### get

```bash
$ tangerine-cli get key
```./src/kv/KVServer.java:8: error: cannot find symbol
    private InMemoryStorage storage;
            ^
  symbol:   class InMemoryStorage
  location: class KVServer
./src/kv/KVServer.java:11: error: cannot find symbol
    public KVServer(InMemoryStorage storage, int port) throws IOException {
                    ^
  symbol:   class InMemoryStorage
  location: class KVServer
./src/kv/KVServer.java:60: error: cannot find symbol
        InMemoryStorage storage = new InMemoryStorage();
        ^
  symbol:   class InMemoryStorage
  location: class KVServer
./src/kv/KVServer.java:60: error: cannot find symbol
        InMemoryStorage storage = new InMemoryStorage();
                                      ^
  symbol:   class InMemoryStorage
  location: class KVServer
4 errors

### remove

```bash
$ tangerine-cli remove key
```

### see all

```bash
$ tangerine-cli see all
```

### quit

```bash
$ tangerine-cli quit
```

## License

MIT