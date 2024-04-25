# 🍊 tangerine.
## in-memory vector database

tangerine is a in-memory vector database that is designed to have fast read and write speeds.

tangerine is not limited to vector database, as it has its own key-value store written from scratch in Java.

## Key-Value Store
### Running

Making sh executable
```bash
chmod +x tangerinekv-server.sh
chmod +x tangerinekv-cli.sh
chmod +x tangerinekv-compile.sh
```

> [!NOTE]
> The .java files are already compiled, but to prevent any issues with the classpath, you should compile them first.

```bash
./tangerinekv-compile.sh

```

Running the Tangerine-KV Server

```bash
 ./tangerinekv-server.sh
```

Running the Tangerine-KV Client
```bash
 ./tangerinekv-cli.sh
```

Testing 
```bash
$ set key value
```
if server responds with `OK`, then the client & server is working.

### Commands
#### set

```bash
$ set key value
```

#### get

```bash
$  get key
```

#### remove

```bash
$  remove key
```

#### see all

```bash
$  see all
```

#### quit

```bash
$ quit
```

## License

MIT