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
```

> [!NOTE]
> The .java files are already compiled, but to prevent any issues with the classpath, you should compile them first.

```bash
$ javac src/kv/*.java

```

```bash
$ ./tangerinekv-server.sh
```

```bash
$ ./tangerinekv-cli.sh
```

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